package it.gov.pagopa.tkm.ms.parretriever.batch;

import it.gov.pagopa.tkm.ms.parretriever.client.cards.*;
import it.gov.pagopa.tkm.ms.parretriever.client.cards.model.response.*;
import lombok.extern.log4j.*;
import org.jetbrains.annotations.*;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Log4j2
public class CardPartitioner implements Partitioner {

    @Autowired
    private ParlessCardsClient parlessCardsClient;

    @NotNull
    @Override
    public Map<String, ExecutionContext> partition(@Value("${batch-execution.max-number-of-threads}") int maxNumberOfThreads) {
        Map<String, ExecutionContext> result = new HashMap<>();

        List<ParlessCard> cards = parlessCardsClient.getParlessCards(maxNumberOfThreads);
        int cardsSize = cards.size();

        if (cardsSize < maxNumberOfThreads) {
            maxNumberOfThreads = cardsSize;
        }

        int range = cardsSize / maxNumberOfThreads;
        int fromId = 1;
        int toId = range;

        for (int i = 1; i <= maxNumberOfThreads; i++) {
            ExecutionContext value = new ExecutionContext();
            log.debug("\nStarting : Thread" + i + " from : " + fromId + " to : " + toId);

            value.putInt("from", fromId);
            value.putInt("to", toId);
            value.putString("name", "Thread" + i);
            value.put("cardList", cards.subList(fromId, Math.min(toId, cardsSize)));

            result.put("partition" + i, value);

            fromId = toId + 1;
            toId += range;
            if (i == maxNumberOfThreads && cardsSize % maxNumberOfThreads > 0) {
                toId += 1;
            }
        }
        return result;
    }

}
