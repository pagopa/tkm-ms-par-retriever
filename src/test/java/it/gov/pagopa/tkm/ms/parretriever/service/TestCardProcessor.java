package it.gov.pagopa.tkm.ms.parretriever.service;

import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.*;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.model.response.ParlessCard;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.consentmanager.*;
import it.gov.pagopa.tkm.ms.parretriever.constant.*;
import it.gov.pagopa.tkm.ms.parretriever.service.impl.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestCardProcessor {

    @InjectMocks
    private CardProcessor cardProcessor;

    @Mock
    private ParlessCardsClient parlessCardsClient;

    @Mock
    private ConsentClient consentClient;

    private DefaultBeans testBeans;

    @BeforeEach
    void init() {
        testBeans = new DefaultBeans();
    }

    @Test
    void processParlessCard_process() {
        cardProcessor = new CardProcessor();
        for (ParlessCard card: testBeans.PARLESS_CARDS_LIST) {
            ParlessCard processedCard = cardProcessor.process(card);
            assertEquals(card, processedCard);
        }
    }

    @Test
    void givenEmptyCard_returnNull() {
        cardProcessor = new CardProcessor();
        ParlessCard card = cardProcessor.process(new ParlessCard());
        assertNull(card);
    }

}
