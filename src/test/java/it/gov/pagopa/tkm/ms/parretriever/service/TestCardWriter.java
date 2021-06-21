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
import org.springframework.test.util.ReflectionTestUtils;

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
    void init() throws NoSuchFieldException {
        testBeans = new DefaultBeans();
        CardWriter.class.getDeclaredField("rateLimit").setAccessible(true);
        CardWriter.class.getDeclaredField("amexParRetrieveEnabled").setAccessible(true);
        CardWriter.class.getDeclaredField("mastercardParRetrieveEnabled").setAccessible(true);
        CardWriter.class.getDeclaredField("visaParRetrieveEnabled").setAccessible(true);
        CardWriter.class.getDeclaredField("amexMaxApiClientCallRate").setAccessible(true);
        CardWriter.class.getDeclaredField("mastercardMaxApiClientCallRate").setAccessible(true);
        CardWriter.class.getDeclaredField("visaMaxApiClientCallRate").setAccessible(true);
        CardWriter.class.getDeclaredField("amexClient").setAccessible(true);
        CardWriter.class.getDeclaredField("mastercardClient").setAccessible(true);
        CardWriter.class.getDeclaredField("visaClient").setAccessible(true);
        CardWriter.class.getDeclaredField("mapper").setAccessible(true);
        CardWriter.class.getDeclaredField("producerService").setAccessible(true);
        ReflectionTestUtils.setField(cardWriter, "rateLimit", (double) testBeans.PARLESS_CARDS_LIST.size());
        ReflectionTestUtils.setField(cardWriter, "amexParRetrieveEnabled", true);
        ReflectionTestUtils.setField(cardWriter, "mastercardParRetrieveEnabled", true);
        ReflectionTestUtils.setField(cardWriter, "visaParRetrieveEnabled", true);
        ReflectionTestUtils.setField(cardWriter, "amexMaxApiClientCallRate", 5.0);
        ReflectionTestUtils.setField(cardWriter, "mastercardMaxApiClientCallRate", 5.0);
        ReflectionTestUtils.setField(cardWriter, "visaMaxApiClientCallRate", 5.0);
        ReflectionTestUtils.setField(cardWriter, "amexClient", amexClient);
        ReflectionTestUtils.setField(cardWriter, "mastercardClient", mastercardClient);
        ReflectionTestUtils.setField(cardWriter, "visaClient", visaClient);
        ReflectionTestUtils.setField(cardWriter, "mapper", mapper);
        ReflectionTestUtils.setField(cardWriter, "producerService", producerService);
    }

    @Test
    void writeOnQueue_exceptionCheck() {
        List<ParlessCard> parlessCards = testBeans.PARLESS_CARDS_LIST;
        assertDoesNotThrow(() -> cardWriter.write(parlessCards));
    }

}
