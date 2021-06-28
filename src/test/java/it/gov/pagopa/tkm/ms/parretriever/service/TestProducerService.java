package it.gov.pagopa.tkm.ms.parretriever.service;

import it.gov.pagopa.tkm.ms.parretriever.constant.DefaultBeans;
import it.gov.pagopa.tkm.ms.parretriever.service.impl.ProducerServiceImpl;
import it.gov.pagopa.tkm.service.PgpUtils;
import org.bouncycastle.openpgp.PGPException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestProducerService {

    @InjectMocks
    private ProducerServiceImpl producerService;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private PgpUtils pgpUtils;

    private DefaultBeans testBeans;

    @BeforeEach
    void init() {
        testBeans = new DefaultBeans();
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    //TODO TESTS

    // @Test(expected = PGPException.class)
    public void givenNullMessage_throwsPGPException() throws PGPException {
        given(pgpUtils.encrypt(null)).willThrow(PGPException.class);
        producerService.sendMessage(null);

    }

    // @Test(expected = PGPException.class)
    public void givenMessage_throwsPGPException() throws PGPException {
        given(pgpUtils.encrypt("message")).willThrow(PGPException.class);
        producerService.sendMessage("message");

    }

    @Test
    public void givenMessage_sendMessageInKafkaNullValue() throws PGPException {
        ReflectionTestUtils.setField(producerService, "readQueueTopic", "readQueueTopic");
        when(pgpUtils.encrypt("message")).thenReturn("encrypetdString");
        assertNull( kafkaTemplate.send("readQueueTopic", pgpUtils.encrypt("message")));
    }


    @Test
    public void givenMessage_sendMessageInKafka() throws PGPException {

        ReflectionTestUtils.setField(producerService, "readQueueTopic", "readQueueTopic");
        when(pgpUtils.encrypt("message")).thenReturn("encrypetdString");
        producerService.sendMessage("message");
        verify(kafkaTemplate).send("readQueueTopic", "encrypetdString");
    }

    @Test
    public void sendMessage_validMessage() throws ParseException, PGPException {
        ReflectionTestUtils.setField(producerService, "readQueueTopic", Mockito.anyString());

        when(pgpUtils.encrypt("message")).thenReturn("encrypetdString");

        String encryptedMessage = pgpUtils.encrypt("message");
        producerService.sendMessage(encryptedMessage);
        verify(kafkaTemplate).send(Mockito.anyString(), Mockito.eq(encryptedMessage));
    }


}

