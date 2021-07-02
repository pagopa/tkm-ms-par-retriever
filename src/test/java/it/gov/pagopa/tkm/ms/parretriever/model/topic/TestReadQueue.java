package it.gov.pagopa.tkm.ms.parretriever.model.topic;

import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.model.response.ParlessCardToken;
import it.gov.pagopa.tkm.ms.parretriever.constant.CircuitEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestReadQueue {

    private ReadQueue readQueue;

    @Test
    void checkProperties_setterGetter() {
        readQueue = new ReadQueue();

        readQueue.setCircuit(CircuitEnum.AMEX);
        assertEquals(CircuitEnum.AMEX, readQueue.getCircuit());

        readQueue.setHpan("hpan");
        assertEquals("hpan", readQueue.getHpan());

        readQueue.setPan("pan");
        assertEquals("pan", readQueue.getPan());

        readQueue.setPar("par");
        assertEquals("par", readQueue.getPar());

        Set<ParlessCardToken> tokens = new HashSet<>();
        tokens.add(new ParlessCardToken());
        readQueue.setTokens(tokens);
        assertEquals(tokens, readQueue.getTokens());
    }

    @Test
    void createInstance_complete() {
        Set<ParlessCardToken> tokens = new HashSet<>();
        tokens.add(new ParlessCardToken());

        readQueue = new ReadQueue("pan", "hpan", "par", CircuitEnum.AMEX, tokens);
        ReadQueue queue = new ReadQueue("pan", "hpan", "par", CircuitEnum.AMEX, tokens);

        boolean equals = readQueue.equals(queue);
        assertTrue(equals);

        assertTrue(readQueue.canEqual(queue));

        assertTrue(readQueue.hashCode() != 0);

        assertNotNull(readQueue.toString());
    }

}
