package it.gov.pagopa.tkm.ms.parretriever.service;

import it.gov.pagopa.tkm.ms.parretriever.service.impl.*;
import it.gov.pagopa.tkm.service.*;
import org.bouncycastle.openpgp.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.springframework.kafka.core.*;
import org.springframework.test.util.*;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class TestProducerService {

    @InjectMocks
    private ProducerServiceImpl producerService;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    private final MockedStatic<PgpStaticUtils> pgpStaticUtilsMockedStatic = mockStatic(PgpStaticUtils.class);

    @BeforeEach
    void init() {
        pgpStaticUtilsMockedStatic.when(()-> PgpStaticUtils.encrypt(anyString(), anyString())).thenReturn("encryptedString");
        ReflectionTestUtils.setField(producerService, "readQueueTopic", "readQueueTopic");
        ReflectionTestUtils.setField(producerService, "pgpPublicKey", "TEST_PUBLIC_KEY");
    }

    @AfterAll
    void close() {
        pgpStaticUtilsMockedStatic.close();
    }

    @Test
    public void givenNullMessage_throwsPGPException() throws PGPException {
        given(PgpStaticUtils.encrypt(null, null)).willThrow(PGPException.class);
        producerService.sendMessage(null);
    }

    @Test
    public void givenMessage_throwsPGPException() throws PGPException {
        given(PgpStaticUtils.encrypt("message", "")).willThrow(PGPException.class);
        producerService.sendMessage("message");

    }

    @Test
    public void givenMessage_sendMessageInKafka() throws PGPException {
        producerService.sendMessage("message");
        verify(kafkaTemplate).send("readQueueTopic", "encryptedString");
    }

    @Test
    public void sendMessage_validMessage() throws PGPException {
        String encryptedMessage = PgpStaticUtils.encrypt("message", "publicKey");
        producerService.sendMessage(encryptedMessage);
        verify(kafkaTemplate).send(anyString(), eq(encryptedMessage));
    }

}

