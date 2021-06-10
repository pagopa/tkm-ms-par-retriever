package it.gov.pagopa.tkm.ms.parretriever.service.impl;

import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.*;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.constant.CircuitEnum;
import lombok.extern.log4j.*;
import org.jetbrains.annotations.*;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import javax.persistence.criteria.CriteriaBuilder;
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

    @Value("${batch-execution.max-number-of-cards}")
    private int maxNumberOfCards;

    @Autowired
    private ParlessCardsClient parlessCardsClient;

    @NotNull
    @Override
    public Map<String, ExecutionContext> partition(@Value("${batch-execution.max-number-of-threads}") int maxNumberOfThreads) {

        List<ParlessCard> cards = parlessCardsClient.getParlessCards(maxNumberOfCards);
        int cardsSize = cards.size();

        if (cardsSize == 0) {
            return new HashMap<>();
        }

        Map<String, ExecutionContext> result = new HashMap<>();
        EnumMap<CircuitEnum, Integer> threadsPerCircuit = new EnumMap<>(CircuitEnum.class);

        //numero di carte per ogni circuito
        EnumMap<CircuitEnum, List<ParlessCard>> parlessCardsPerCircuit = new EnumMap<>(CircuitEnum.class);

        //Popolamento map circuito/carte
        for (int i = 0; i < cardsSize; i++) {
            CircuitEnum circuit = cards.get(i).getCircuit();

            if (!parlessCardsPerCircuit.containsKey(circuit)) {
                List<ParlessCard> circuitCards = Stream.of(cards.get(i)).collect(Collectors.toList());
                parlessCardsPerCircuit.put(circuit, circuitCards);
            } else {
                parlessCardsPerCircuit.get(circuit).add(cards.get(i));
            }
        }

        //Non posso avere un numero di thread superiore alla somma
        //del numero di chiamate al secondo per ciascun circuito
        //Quindi se ho un numero di thread superiore
        //do ad ogni circuito il max numero di thread possibile senza fare calcoli
        if (maxNumberOfThreads > maxNumberOfThreadsFromCircuitLimits()) {
            for (CircuitEnum circuitEnum : CircuitEnum.values()) {
                threadsPerCircuit.put(circuitEnum, getApiCallMaxRateByCircuit(circuitEnum).intValue());
            }
        } else {
            //tempo di elaborazione previsto per ogni circuito con thread singolo
            EnumMap<CircuitEnum, Double> singleThreadElaborationTimePerCircuit = new EnumMap<>(CircuitEnum.class);

            parlessCardsPerCircuit.keySet().forEach(c -> {
                singleThreadElaborationTimePerCircuit.put(c, parlessCardsPerCircuit.get(c).size() /
                        getApiCallMaxRateByCircuit(c));
            });

            double totalElaborationTime =
                    singleThreadElaborationTimePerCircuit.values().stream().reduce(0d, Double::sum);

            threadsPerCircuit = normalizeNumberOfThreads(singleThreadElaborationTimePerCircuit,
                    totalElaborationTime, maxNumberOfThreads);
        }

        //creazione degli execution context (thread) per ciascun circuito
        int circuitIndex=0;
        for (CircuitEnum circuit : parlessCardsPerCircuit.keySet()) {

            List<ParlessCard> circuitCards = parlessCardsPerCircuit.get(circuit);
            int cardsSizeByCircuit = circuitCards.size();
            int maxNumberOfThreadsByCircuit = threadsPerCircuit.get(circuit);
            //se il numero di carte è inferiore al numero di thread, crea un thread per carta
            if (cardsSizeByCircuit <= maxNumberOfThreadsByCircuit) {
                maxNumberOfThreadsByCircuit = cardsSizeByCircuit;
            }

            int[] subListIndexes = subListIndexes(cardsSizeByCircuit, maxNumberOfThreadsByCircuit);
            double apiCallMaxRateByCircuit = getApiCallMaxRateByCircuit(circuit);

            //Sempre almeno una chiamata al secondo
            double maxApiRatePerExecutionContext =
                    Math.max(1, apiCallMaxRateByCircuit / maxNumberOfThreadsByCircuit);

            for (int i = 1; i < subListIndexes.length; i++) {

                int fromId = subListIndexes[i - 1];
                int toId = subListIndexes[i];
                ExecutionContext value = new ExecutionContext();
                log.debug("\nStarting : Thread" + i + " from : " + fromId + " to : " + toId);

                value.putInt("from", fromId);
                value.putInt("to", toId);
                value.putString("name", "Thread" + i);
                value.put("cardList", new ArrayList<>(circuitCards.subList(fromId, Math.min(toId, cardsSizeByCircuit))));
                value.put("rateLimit", maxApiRatePerExecutionContext);
                int j =i+circuitIndex;
                result.put("partition" + j, value);

            }
            circuitIndex=circuitIndex+subListIndexes.length-1;
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

    private Double maxNumberOfThreadsFromCircuitLimits(){
        return amexMaxApiClientCallRate + mastercardMaxApiClientCallRate + visaMaxApiClientCallRate;
    }

    //Distribuzione del numero di thread ad ogni circuito
    private EnumMap<CircuitEnum, Integer> normalizeNumberOfThreads(Map<CircuitEnum, Double> elaborationTimes,
                                                               Double totalElaborationTime, int maxNumberOfThreads) {

        //Assegnazione del numero di thread a ciascun circuito distribuendo secondo la proporzione
        //numero_di_thread_circuito= (tempo_di_elaborazione_circuito/tempo_totale_elaborazione)*numero_max_thread
        //valori arrotondati ad intero
        EnumMap<CircuitEnum, Integer> threadsPerCircuit = new EnumMap<>(CircuitEnum.class);
        EnumMap<CircuitEnum, Integer> underLimitCircuits = new EnumMap<>(CircuitEnum.class);

        for (Map.Entry<CircuitEnum, Double> entry : elaborationTimes.entrySet()) {
            double doubleNumberOfThreads = (entry.getValue() / totalElaborationTime) * maxNumberOfThreads;
            threadsPerCircuit.put(entry.getKey(), Math.max((int) Math.round(doubleNumberOfThreads), 1));
        }
        int threadsUsedByCircuits = 0;
        double threadsUsedByUnderLoadedCircuits = 0;

        for (Map.Entry<CircuitEnum, Integer> entry : threadsPerCircuit.entrySet()) {
            int threadPerCircuit = entry.getValue();
            int apiCallMaxRateByCircuit = getApiCallMaxRateByCircuit(entry.getKey()).intValue();
            if (threadPerCircuit >= apiCallMaxRateByCircuit) {
                threadPerCircuit = apiCallMaxRateByCircuit;
                threadsUsedByCircuits += threadPerCircuit;
            } else {
                underLimitCircuits.put(entry.getKey(), threadPerCircuit);
                threadsUsedByCircuits += threadPerCircuit;
                threadsUsedByUnderLoadedCircuits += threadPerCircuit;
            }
            threadsPerCircuit.put(entry.getKey(), threadPerCircuit);
        }

        //Numero di thread ancora disponibili da distribuire
        int threadSurplus = maxNumberOfThreads - threadsUsedByCircuits;

        //Uno o più circuiti potrebbero avere un numero di thread calcolato superiore a quelli che possono gestire
        //in questo caso, il loro numero di thread viene posto al massimo possibile
        //e i thread in più che erano stati assegnati loro vengono distribuiti agli altri circuiti
        for (Map.Entry<CircuitEnum, Integer> entry : underLimitCircuits.entrySet()) {

            double addedThreadD = Math.ceil(entry.getValue() / threadsUsedByUnderLoadedCircuits * (double) threadSurplus);
            int addedThread = (int) addedThreadD;
            int apiCallMaxRateByCircuit = getApiCallMaxRateByCircuit(entry.getKey()).intValue();
            int calculatedValue = threadsPerCircuit.get(entry.getKey()) + addedThread;
            int allowedValue = Math.min(calculatedValue, apiCallMaxRateByCircuit);
            threadsUsedByCircuits += addedThread;

            if (threadsUsedByCircuits > maxNumberOfThreads) {
                allowedValue--;
            }
            threadsPerCircuit.put(entry.getKey(), allowedValue);
        }

        return threadsPerCircuit;
    }

    private int[] subListIndexes(Integer numberOfParlessCards, Integer maxNumberOfThread) {
        if (numberOfParlessCards<=maxNumberOfThread){
            maxNumberOfThread=numberOfParlessCards;
        }

        int ratio = numberOfParlessCards / maxNumberOfThread;
        int subListCoveredByRatio = ratio * maxNumberOfThread;
        int[] subListIndexes = new int[maxNumberOfThread + 1];
        subListIndexes[0] = 0;
        int remaining = numberOfParlessCards - subListCoveredByRatio;

        for (int i = 1; i < remaining + 1; i++) {
            subListIndexes[i] = subListIndexes[i - 1] + ratio + 1;
        }

        for (int i = remaining + 1; i < maxNumberOfThread + 1; i++) {
            if (i==maxNumberOfThread) ratio =ratio+1;
            subListIndexes[i] = subListIndexes[i - 1] + ratio;

        }
        return subListIndexes;

    }

}
