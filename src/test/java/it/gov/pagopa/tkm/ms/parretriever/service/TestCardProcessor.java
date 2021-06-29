package it.gov.pagopa.tkm.ms.parretriever.service;

import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.*;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.model.response.ParlessCard;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.consentmanager.*;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.consentmanager.model.response.ConsentResponse;
import it.gov.pagopa.tkm.ms.parretriever.constant.*;
import it.gov.pagopa.tkm.ms.parretriever.service.impl.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestCardProcessor {

    @InjectMocks
    private CardProcessor cardProcessor;

    @Mock
    private ParlessCardsClient parlessCardsClient;

    @Mock
    private ConsentClient consentClient;

    @Mock
    private CryptoService cryptoService;

    private DefaultBeans testBeans;

    @BeforeEach
    void init() {
        testBeans = new DefaultBeans();
    }

    @Test
    void processParlessCard_processAllow() {
        for (ParlessCard card : testBeans.PARLESS_CARDS_LIST) {
            ParlessCard processedCard = null;
            if (consentClient.getConsent(card.getTaxCode(), card.getHpan(), null) != null) {
                processedCard = cardProcessor.process(card);
                assertEquals(card, processedCard);
                 System.out.println("CONSENT IS NOT NULL");
            } else {
                when(consentClient.getConsent(card.getTaxCode(), card.getHpan(), null) ).thenReturn(testBeans.CARD_CONSENT_RESPONSE_ALLOW);
                processedCard = cardProcessor.process(card);
                assertEquals(card, processedCard);
            }
        }
    }

    @Test
    void processParlessCard_processDeny() {
        for (ParlessCard card : testBeans.PARLESS_CARDS_LIST) {
            ParlessCard processedCard = null;
            if (consentClient.getConsent(card.getTaxCode(), card.getHpan(), null) != null) {
                processedCard = cardProcessor.process(card);
                assertEquals(card, processedCard);
            } else {
                when(consentClient.getConsent(card.getTaxCode(), card.getHpan(), null) ).thenReturn(testBeans.CARD_CONSENT_RESPONSE_DENY);
                processedCard = cardProcessor.process(card);
                assertEquals(null, processedCard);
            }
        }
    }


    @Test
    void processParlessCard_processPartial() {
        for (ParlessCard card : testBeans.PARLESS_CARDS_LIST) {
            ParlessCard processedCard = null;
            if (consentClient.getConsent(card.getTaxCode(), card.getHpan(), null) != null) {
                processedCard = cardProcessor.process(card);
                assertEquals(card, processedCard);
            } else {
                when(consentClient.getConsent(card.getTaxCode(), card.getHpan(), null) ).thenReturn(testBeans.CARD_CONSENT_RESPONSE_PARTIAL);
                processedCard = cardProcessor.process(card);
                assertEquals(card, processedCard);
            }
        }
    }


    @Test
    void badProcessing_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> cardProcessor.process(testBeans.PARLESS_CARD_1));
    }

}
