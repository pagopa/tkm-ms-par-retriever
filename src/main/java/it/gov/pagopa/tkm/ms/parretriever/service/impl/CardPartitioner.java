package it.gov.pagopa.tkm.ms.parretriever.service.impl;

import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.*;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.constant.CircuitEnum;
import lombok.extern.log4j.*;
import org.jetbrains.annotations.*;
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

        if (maxNumberOfThreads == 0) {
            return new HashMap<>();
        }

        List<ParlessCard> cards = parlessCardsClient.getParlessCards(maxNumberOfCards);
        int cardsSize = cards == null ? 0 : cards.size();

        log.debug("Number of parless cards: " + cardsSize);

        if (cardsSize == 0) {
            return new HashMap<>();
        }

        Map<String, ExecutionContext> result = new TreeMap<>();
        Map<CircuitEnum, Integer> threadsPerCircuit = new TreeMap<>();

        //numero di carte per ogni circuito
        Map<CircuitEnum, List<ParlessCard>> parlessCardsPerCircuit = new TreeMap<>();

        //Popolamento map circuito/carte
        for (int i = 0; i < cardsSize; i++) {
            CircuitEnum circuit = groupVisaCircuitEnum(cards.get(i).getCircuit());

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
        if (maxNumberOfThreads >= maxNumberOfThreadsFromCircuitLimits(parlessCardsPerCircuit.keySet())) {
            for (CircuitEnum circuitEnum : parlessCardsPerCircuit.keySet()) {
                threadsPerCircuit.put(circuitEnum, getApiCallMaxRateByCircuit(circuitEnum).intValue());
            }
        } else {
            //tempo di elaborazione previsto per ogni circuito con thread singolo
            Map<CircuitEnum, Double> singleThreadElaborationTimePerCircuit = new HashMap<>();

            parlessCardsPerCircuit.keySet().forEach(c -> {
                singleThreadElaborationTimePerCircuit.put(c, parlessCardsPerCircuit.get(c).size() /
                        getApiCallMaxRateByCircuit(c));
            });

            double totalElaborationTime =
                    singleThreadElaborationTimePerCircuit.values().stream().reduce(0d, Double::sum);

            threadsPerCircuit = normalizeNumberOfThreadsV2(singleThreadElaborationTimePerCircuit,
                    totalElaborationTime, maxNumberOfThreads);
        }

        //creazione degli execution context (thread) per ciascun circuito
        int circuitIndex = 0;
        for (Map.Entry<CircuitEnum, List<ParlessCard>> entry : parlessCardsPerCircuit.entrySet()) {
            CircuitEnum circuit = entry.getKey();

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
                int j = i + circuitIndex;

                value.putInt("from", fromId);
                value.putInt("to", toId);
                value.putString("name", "Thread" + j);
                value.put("cardList", new ArrayList<>(circuitCards.subList(fromId, Math.min(toId, cardsSizeByCircuit))));
                value.put("rateLimit", maxApiRatePerExecutionContext);
                result.put("partition" + j, value);

            }
            circuitIndex = circuitIndex + subListIndexes.length - 1;
        }

        return result;
    }

    private Double getApiCallMaxRateByCircuit(CircuitEnum circuit) {
        if (circuit == null) {
            return 1d;
        }

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

    private double maxNumberOfThreadsFromCircuitLimits(Set<CircuitEnum> circuits) {
        double total = 0d;
        for (CircuitEnum circuit : circuits) {
            CircuitEnum actualCircuitEnum = groupVisaCircuitEnum(circuit);
            total += getApiCallMaxRateByCircuit(actualCircuitEnum);
        }
        return total;
    }

    //Distribuzione del numero di thread ad ogni circuito
    private Map<CircuitEnum, Integer> normalizeNumberOfThreadsV2(Map<CircuitEnum, Double> elaborationTimes,
                                                                 Double totalElaborationTime, int maxNumberOfThreads) {

        //Assegnazione del numero di thread a ciascun circuito distribuendo secondo la proporzione
        //numero_di_thread_circuito= (tempo_di_elaborazione_circuito/tempo_totale_elaborazione)*numero_max_thread
        //valori arrotondati ad intero
        Map<CircuitEnum, Integer> threadsPerCircuit = new TreeMap<>();
        Set<CircuitEnum> filledCircuits = new HashSet<>();

        int numberOfThreads = maxNumberOfThreads;
        int threadsUsedByCircuits = 0;

        double elaborationTimeCounter = totalElaborationTime;
        double elaborationTime;

        //Alcuni circuiti potrebbero avere numero di thread assegnati superiore al loro massimo
        //In questo caso il loro numero di thread viene posto al massimo
        //e i thread in più che avevano vengono distribuiti tra gli altri circuiti
        //Il processo si ripete finchè tutti i thread disponibili sono stati assegnati
        while (numberOfThreads > 0) {
            elaborationTime = elaborationTimeCounter;
            threadsUsedByCircuits = 0;
            for (Map.Entry<CircuitEnum, Double> entry : elaborationTimes.entrySet()) {

                int apiCallMaxRateByCircuit = getApiCallMaxRateByCircuit(entry.getKey()).intValue();
                double doubleNumberOfThreads = (entry.getValue() / elaborationTime) * numberOfThreads;

                int calculatedValue = Math.max((int) Math.round(doubleNumberOfThreads), 1);
                int totalValue = (threadsPerCircuit.get(entry.getKey()) == null ?
                        0 : threadsPerCircuit.get(entry.getKey())) + calculatedValue;

                if (!filledCircuits.contains(entry.getKey())) {
                    if (totalValue >= apiCallMaxRateByCircuit) {
                        calculatedValue = apiCallMaxRateByCircuit;
                        filledCircuits.add(entry.getKey());
                        threadsPerCircuit.put(entry.getKey(), calculatedValue);
                        elaborationTimeCounter -= entry.getValue();
                    } else {
                        int currentValue = threadsPerCircuit.get(entry.getKey()) == null ?
                                0 : threadsPerCircuit.get(entry.getKey());
                        threadsPerCircuit.put(entry.getKey(), currentValue + calculatedValue);
                    }
                }
                threadsUsedByCircuits += threadsPerCircuit.get(entry.getKey());
            }
            numberOfThreads = maxNumberOfThreads - threadsUsedByCircuits;
        }

        //A causa dell'arrotondamento dei valori <1  ad 1,
        // la somma del numero di thread usati potrebbe essere superiore al max numero
        //di thread ammessi - ciclo di correzione
        while (threadsUsedByCircuits > maxNumberOfThreads) {
            for (Map.Entry<CircuitEnum, Integer> entry : threadsPerCircuit.entrySet()) {
                if (entry.getValue() > 1 && threadsUsedByCircuits > maxNumberOfThreads) {
                    threadsPerCircuit.put(entry.getKey(), entry.getValue() - 1);
                    threadsUsedByCircuits--;
                }
            }
        }

        return threadsPerCircuit;
    }

    private int[] subListIndexes(Integer numberOfParlessCards, Integer maxNumberOfThread) {
        if (numberOfParlessCards <= maxNumberOfThread) {
            maxNumberOfThread = numberOfParlessCards;
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
            subListIndexes[i] = subListIndexes[i - 1] + ratio;

        }
        return subListIndexes;

    }

    private CircuitEnum groupVisaCircuitEnum(CircuitEnum circuit) {
        switch (circuit) {
            case AMEX:
                return CircuitEnum.AMEX;
            case MASTERCARD:
                return CircuitEnum.MASTERCARD;
            case VISA:
            case VISA_ELECTRON:
            case VPAY:
                return CircuitEnum.VISA;
            default:
                return null;
        }
    }

}

