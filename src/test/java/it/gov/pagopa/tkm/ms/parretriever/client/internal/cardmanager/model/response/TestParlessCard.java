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

        parlessCard.setTaxCode("taxCode");
        assertEquals("taxCode", parlessCard.getTaxCode());

        Set<String> tokens = new HashSet<>();
        tokens.add("token");
        parlessCard.setTokens(tokens);
        assertEquals(tokens, parlessCard.getTokens());
    }

    @Test
    void createInstance_complete() {
        Set<String> tokens = new HashSet<>();
        tokens.add("token");

        parlessCard = new ParlessCard("taxCode", "pan", "hpan", tokens, CircuitEnum.AMEX);
        ParlessCard card = new ParlessCard("taxCode", "pan", "hpan", tokens, CircuitEnum.AMEX);

        boolean equals = parlessCard.equals(card);
        assertTrue(equals);

        assertTrue(parlessCard.canEqual(card));

        assertTrue(parlessCard.hashCode() != 0);

        assertNotNull(parlessCard.toString());
    }

}
