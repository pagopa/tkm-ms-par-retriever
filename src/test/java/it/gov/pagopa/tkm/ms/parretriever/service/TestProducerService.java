package it.gov.pagopa.tkm.ms.parretriever.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.gov.pagopa.tkm.ms.parretriever.config.KafkaConfig;
import it.gov.pagopa.tkm.ms.parretriever.constant.DefaultBeans;
import it.gov.pagopa.tkm.ms.parretriever.service.impl.ProducerServiceImpl;
import it.gov.pagopa.tkm.service.PgpUtils;
import okhttp3.mockwebserver.MockWebServer;
import org.bouncycastle.openpgp.PGPException;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@TestConfiguration
public class TestProducerService {

    @InjectMocks
    private ProducerServiceImpl producerService;

    @Mock
    private KafkaConfig kafkaConfig;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private PgpUtils pgpUtils;

    private static MockWebServer mockServer;

    private DefaultBeans testBeans;

    @Bean
    KafkaConfig createKafkaConfig() {
        kafkaConfig = new KafkaConfig();
        return kafkaConfig;
    }

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


//    @Test
//    public void givenMessage_sendMessageInKafka() throws PGPException {
//
//        ReflectionTestUtils.setField(producerService, "readQueueTopic", "readQueueTopic");
//        when(pgpUtils.encrypt("message")).thenReturn("encrypetdString");
//        producerService.sendMessage("message");
//        verify(kafkaTemplate).send("readQueueTopic", "encrypetdString");
//    }
//
//    @Test
//    public void sendMessage_validMessage() throws ParseException, PGPException {
//        ReflectionTestUtils.setField(producerService, "readQueueTopic", Mockito.anyString());
//
//        when(pgpUtils.encrypt("message")).thenReturn("encrypetdString");
//
//        String encryptedMessage = pgpUtils.encrypt("message");
//        producerService.sendMessage(encryptedMessage);
//        verify(kafkaConfig.kafkaTemplate()).send(Mockito.anyString(), Mockito.eq(encryptedMessage));
//    }


}
