package it.gov.pagopa.tkm.ms.parretriever.service;

import it.gov.pagopa.tkm.ms.parretriever.constant.*;
import it.gov.pagopa.tkm.ms.parretriever.service.impl.*;
import it.gov.pagopa.tkm.service.*;
import org.bouncycastle.openpgp.PGPException;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.springframework.kafka.core.*;
//import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;


import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
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

    @Test(expected = PGPException.class)
    public void givenNullMessage_throwsPGPException() throws PGPException {
        given(pgpUtils.encrypt(null)).willThrow(PGPException.class);
        producerService.sendMessage(null);

    }

    @Test(expected = PGPException.class)
    public void givenMessage_throwsPGPException() throws PGPException {
        given(pgpUtils.encrypt("message")).willThrow(PGPException.class);
        producerService.sendMessage("message");

    }

    @Test
    public void givenMessage_sendMessageInKafka() throws PGPException {
        ReflectionTestUtils.setField(producerService, "readQueueTopic", "readQueueTopic");
        when(pgpUtils.encrypt("message")).thenReturn("encrypetdString");
        producerService.sendMessage("message");
        verify(kafkaTemplate).send("readQueueTopic", "encrypetdString");
    }



}
