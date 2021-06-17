package it.gov.pagopa.tkm.ms.parretriever.service;

import com.fasterxml.jackson.databind.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.amex.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.visa.*;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.model.response.ParlessCard;
import it.gov.pagopa.tkm.ms.parretriever.constant.*;
import it.gov.pagopa.tkm.ms.parretriever.service.impl.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestCardWriter {

    @InjectMocks
    private CardWriter cardWriter;

    @Mock
    private ProducerServiceImpl producerService;

    @Mock
    private VisaClient visaClient;

    @Mock
    private MastercardClient mastercardClient;

    @Mock
    private AmexClient amexClient;

    private ObjectMapper mapper = new ObjectMapper();

    private DefaultBeans testBeans;

    @BeforeEach
    void init() {
        testBeans = new DefaultBeans();
    }

    @Test
    void writeOnQueue_exceptionCheck() {
        List<ParlessCard> parlessCards = testBeans.PARLESS_CARDS_LIST;
        cardWriter = new CardWriter(parlessCards, (double) parlessCards.size());
        assertDoesNotThrow(() -> cardWriter.write(parlessCards));
    }

}
