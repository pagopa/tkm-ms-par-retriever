package it.gov.pagopa.tkm.ms.parretriever.service;

import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.constant.*;
import it.gov.pagopa.tkm.ms.parretriever.service.impl.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class TestCardReader {

    private CardReader cardReader;

    private final DefaultBeans testBeans = new DefaultBeans();

    @Test
    void givenParlessCardsList_readAll() {
        cardReader = new CardReader(testBeans.PARLESS_CARDS_LIST);
        for (ParlessCard c : testBeans.PARLESS_CARDS_LIST) {
            ParlessCard readCard = cardReader.read();
            assertEquals(c, readCard);
        }
    }

    @Test
    void givenEmptyList_returnNull() {
        cardReader = new CardReader(new ArrayList<>());
        assertNull(cardReader.read());
        cardReader = new CardReader(null);
        assertNull(cardReader.read());
    }

}
