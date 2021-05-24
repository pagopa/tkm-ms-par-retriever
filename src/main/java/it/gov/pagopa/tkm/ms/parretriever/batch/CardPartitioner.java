package it.gov.pagopa.tkm.ms.parretriever.batch;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardPartitioner implements Partitioner {

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> result = new HashMap<>();

        //Chiamo S2 per le carte
        List</*TODO Card*/Object> cards = new ArrayList<>();

        int range = /*numero carte diviso gridSize*/10;
        int fromId = 1;
        int toId = range;

        for (int i = 1; i <= gridSize; i++) {
            ExecutionContext value = new ExecutionContext();

            System.out.println("\nStarting : Thread" + i);
            System.out.println("from : " + fromId);
            System.out.println("to : " + toId);

            value.putInt("from", fromId);
            value.putInt("to", toId);

            // give each thread a name, thread 1,2,3
            value.putString("name", "Thread" + i);

            value.put("cardList", cards.subList(fromId, Math.min(toId, cards.size())));

            result.put("partition" + i, value);

            fromId = toId + 1;
            toId += range;
        }

        return result;
    }

}
