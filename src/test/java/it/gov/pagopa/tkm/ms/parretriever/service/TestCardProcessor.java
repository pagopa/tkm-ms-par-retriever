package it.gov.pagopa.tkm.ms.parretriever.service;

import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.*;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.constant.*;
import it.gov.pagopa.tkm.ms.parretriever.service.impl.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.springframework.test.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class TestCardProcessor {

    @InjectMocks
    private CardProcessor cardProcessor;

    @Mock
    private ParlessCardsClient parlessCardsClient;

    @Mock
    private CryptoService cryptoService;

    private DefaultBeans testBeans;

    @BeforeEach
    void init() {
        testBeans = new DefaultBeans();
        ReflectionTestUtils.setField(cardProcessor, "isAmexActive", "true");
        ReflectionTestUtils.setField(cardProcessor, "isMastercardActive", "true");
        ReflectionTestUtils.setField(cardProcessor, "isVisaActive", "true");
    }

    @Test
    void processParlessCard_processAllow() {
        for (ParlessCard card : testBeans.PARLESS_CARDS_LIST_ALL_CIRCUITS) {
            ParlessCard processedCard;
            processedCard = cardProcessor.process(card);
            assertEquals(card, processedCard);
        }
    }

    @Test
    void processParlessCard_processNotAllow() {
        ReflectionTestUtils.setField(cardProcessor, "isAmexActive", "false");
        ReflectionTestUtils.setField(cardProcessor, "isMastercardActive", "false");
        ReflectionTestUtils.setField(cardProcessor, "isVisaActive", "false");
        for (ParlessCard card : testBeans.PARLESS_CARDS_LIST_ALL_CIRCUITS) {
            ParlessCard processedCard;
            processedCard = cardProcessor.process(card);
            assertNull(processedCard);
        }
    }

    @Test
    void processParlessCard_processNoCircuit() {
        for (ParlessCard card : testBeans.PARLESS_CARDS_LIST_ALL_CIRCUITS) {
            ParlessCard processedCard;
            card.setCircuit(CircuitEnum.MAESTRO);
            processedCard = cardProcessor.process(card);
            assertNull(processedCard);
        }
    }

}
