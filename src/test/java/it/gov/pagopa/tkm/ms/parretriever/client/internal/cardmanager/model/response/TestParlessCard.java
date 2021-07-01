package it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.model.response;

import it.gov.pagopa.tkm.ms.parretriever.constant.CircuitEnum;
import it.gov.pagopa.tkm.ms.parretriever.model.topic.Token;
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
public class TestParlessCard {

    private ParlessCard parlessCard;

    @Test
    void checkProperties_setterGetter() {
        parlessCard = new ParlessCard();

        parlessCard.setHpan("hpan");
        assertEquals("hpan", parlessCard.getHpan());

        parlessCard.setPan("pan");
        assertEquals("pan", parlessCard.getPan());

        parlessCard.setCircuit(CircuitEnum.AMEX);
        assertEquals(CircuitEnum.AMEX, parlessCard.getCircuit());

        Set<ParlessCardToken> tokens = new HashSet<>();
        tokens.add(new ParlessCardToken("token", "3c469e9d6c5875d37a43f353d4f88e61fcf812c66eee3457465a40b0da4153e0"));
        parlessCard.setTokens(tokens);
        assertEquals(tokens, parlessCard.getTokens());
    }

    @Test
    void createInstance_complete() {
        Set<ParlessCardToken> tokens = new HashSet<>();
        tokens.add(new ParlessCardToken("token", "3c469e9d6c5875d37a43f353d4f88e61fcf812c66eee3457465a40b0da4153e0"));

        parlessCard = new ParlessCard("pan", "hpan", CircuitEnum.AMEX, tokens);
        ParlessCard card = new ParlessCard("pan", "hpan", CircuitEnum.AMEX, tokens);

        boolean equals = parlessCard.equals(card);
        assertTrue(equals);

        assertTrue(parlessCard.canEqual(card));

        assertTrue(parlessCard.hashCode() != 0);

        assertNotNull(parlessCard.toString());
    }

}
