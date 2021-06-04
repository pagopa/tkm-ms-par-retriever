package it.gov.pagopa.tkm.ms.parretriever.service.impl;

import com.google.common.util.concurrent.RateLimiter;
import it.gov.pagopa.tkm.ms.parretriever.client.cards.*;
import it.gov.pagopa.tkm.ms.parretriever.client.cards.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.constant.CircuitEnum;
import lombok.extern.log4j.*;
import org.jetbrains.annotations.*;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Log4j2
public class CardPartitioner implements Partitioner {


    @Value("${batch-execution.amex-par-retrieve-enabled}")
    private Boolean amexParRetrieveEnabled;

    @Value("${batch-execution.mastercard-par-retrieve-enabled}")
    private Boolean mastercardParRetrieveEnabled;

    @Value("${batch-execution.visa-par-retrieve-enabled}")
    private Boolean visaParRetrieveEnabled;

    @Value("${batch-execution.amex-max-api-client-call-rate}")
    private Double amexMaxApiClientCallRate;

    @Value("${batch-execution.mastercard-max-api-client-call-rate}")
    private Double mastercardMaxApiClientCallRate;

    @Value("${batch-execution.visa-max-api-client-call-rate}")
    private Double visaMaxApiClientCallRate;

    @Autowired
    private ParlessCardsClient parlessCardsClient;

    @NotNull
    @Override
    public Map<String, ExecutionContext> partition(@Value("${batch-execution.max-number-of-threads}") int maxNumberOfThreads) {

        Map<String, ExecutionContext> result = new HashMap<>();

        List<ParlessCard> cards = parlessCardsClient.getParlessCards(maxNumberOfThreads);
        int cardsSize = cards.size();

        //fallback
        if (cardsSize == 0) {
            return new HashMap<>();
        }

        //numero di carte per ogni circuito
        EnumMap<CircuitEnum, List<ParlessCard>> parlessCardsPerCircuit = new EnumMap<>(CircuitEnum.class);
        //tempo di elaborazione previsto per ogni circuito con thread singolo
        EnumMap<CircuitEnum, Double> singleThreadElaborationTimePerCircuit = new EnumMap<>(CircuitEnum.class);

        //Popolamento map circuito/carte
        for (int i = 0; i < cardsSize; i++) {
            CircuitEnum circuit = cards.get(i).getCircuit();

            if (!parlessCardsPerCircuit.containsKey(circuit)) {
                List<ParlessCard> circuitCards = Stream.of(cards.get(i)).collect(Collectors.toList());
                ;
                parlessCardsPerCircuit.put(circuit, circuitCards);
            } else {
                parlessCardsPerCircuit.get(circuit).add(cards.get(i));
            }
        }

        //Popolamento map circuito/tempo di elaborazione per thread singolo
        parlessCardsPerCircuit.keySet().forEach(c -> {
            singleThreadElaborationTimePerCircuit.put(c, parlessCardsPerCircuit.get(c).size() /
                    getApiCallMaxRateByCircuit(c));
        });

        //Tempo totale di elaborazione
        double totalElaborationTime =
                singleThreadElaborationTimePerCircuit.values().stream().reduce(0d, Double::sum);

        //Distribuzione del numero di thread tra i circuiti
        Map<CircuitEnum, Integer> threadsPerCircuit = normalizeNumberOfThreads(singleThreadElaborationTimePerCircuit,
                totalElaborationTime, maxNumberOfThreads);

        //creazione degli execution context (thread) per ciascun circuito
        for (CircuitEnum circuit : parlessCardsPerCircuit.keySet()) {

            int cardSizeByCircuit = parlessCardsPerCircuit.get(circuit).size();
            int maxNumberOfThreadsByCircuit = threadsPerCircuit.get(circuit);
            int[] subListIndexes = subListIndexes(cardSizeByCircuit, maxNumberOfThreadsByCircuit);
            ;

            for (int i = 1; i < subListIndexes.length; i++) {
                int fromId = subListIndexes[i - 1];
                int toId = subListIndexes[i];

                ExecutionContext value = new ExecutionContext();
                log.debug("\nStarting : Thread" + i + " from : " + fromId + " to : " + toId);
                double maxApiRatePerExecutionContext = getApiCallMaxRateByCircuit(circuit) / maxNumberOfThreadsByCircuit;

                value.putInt("from", fromId);
                value.putInt("to", toId);
                value.putString("name", "Thread" + i);
                value.put("cardList", cards.subList(fromId, Math.min(toId, cardsSize)));
                value.put("rateLimiter", RateLimiter.create(Math.floor(maxApiRatePerExecutionContext)));
                result.put("partition" + i, value);

            }
        }
        return result;
    }

    private Double getApiCallMaxRateByCircuit(CircuitEnum circuit) {
        switch (circuit) {
            case AMEX:
                return amexMaxApiClientCallRate;
            case MASTERCARD:
                return mastercardMaxApiClientCallRate;
            case VISA:
            case VISA_ELECTRON:
            case VPAY:
                return visaMaxApiClientCallRate;
            default:
                return 1d;
        }

    }

    //Distribuzione del numero di thread in base al tempo previsto di elaborazione
    private Map<CircuitEnum, Integer> normalizeNumberOfThreads(Map<CircuitEnum, Double> elaborationTimes,
                                                               Double totalElaborationTime, int maxNumberOfThreads) {

        //Assegnazione del numero di thread per ciascun circuito distribuendo secondo la proporzione
        // numero_di_thread_circuito= (tempo_di_elaborazione_circuito/tempo_totale_elaborazione)*numero_max_thread
        //valori arrotondati all'intero inferiore
        EnumMap<CircuitEnum, Integer> threadsPerCircuit = new EnumMap<>(CircuitEnum.class);
        for (Map.Entry<CircuitEnum, Double> entry : elaborationTimes.entrySet()) {
            double doubleNumberOfThreads = (entry.getValue() / totalElaborationTime) * maxNumberOfThreads;
            int downRoundedNumberOfThreads = (int) doubleNumberOfThreads;
            threadsPerCircuit.put(entry.getKey(), downRoundedNumberOfThreads);
        }

        //ordinamento dei circuiti da quello che ha meno thread a quello che ne ha di più
        Map<CircuitEnum, Integer> sortedThreadsPerCircuit = threadsPerCircuit.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        //aggiunta di un thread per circuito da quello che ne ha meno a quello che ne ha di più fino
        //a che la somma dei thread è pari al numero massimo di thread
        for (Map.Entry<CircuitEnum, Integer> entry : sortedThreadsPerCircuit.entrySet()) {
            if (threadsPerCircuit.values().stream().reduce(0, Integer::sum) < maxNumberOfThreads) {
                threadsPerCircuit.put(entry.getKey(), entry.getValue() + 1);
            }
        }

        return threadsPerCircuit;
    }

    private int[] subListIndexes(Integer numberOfParlessCards, Integer maxNumberOfThread) {
        int ratio = numberOfParlessCards / maxNumberOfThread;
        int subListCoveredByRatio = ratio * maxNumberOfThread;
        int[] subListIndexes = new int[maxNumberOfThread + 1];
        subListIndexes[0] = 0;
        int remaining = numberOfParlessCards - subListCoveredByRatio;

        for (int i = 1; i < remaining + 1; i++) {
            subListIndexes[i] = subListIndexes[i - 1] + ratio + 1;
        }

        for (int i = remaining + 1; i < maxNumberOfThread + 1; i++) {
            subListIndexes[i] = subListIndexes[i - 1] + ratio;
        }
        return subListIndexes;

    }
}
