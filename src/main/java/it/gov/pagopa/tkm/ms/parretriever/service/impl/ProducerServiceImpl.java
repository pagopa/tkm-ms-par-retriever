package it.gov.pagopa.tkm.ms.parretriever.service.impl;

import it.gov.pagopa.tkm.ms.parretriever.config.KafkaConfig;
import it.gov.pagopa.tkm.ms.parretriever.service.*;
import it.gov.pagopa.tkm.service.*;
import lombok.extern.log4j.Log4j2;
import org.bouncycastle.openpgp.PGPException;
import org.springframework.beans.factory.annotation.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public final class ProducerServiceImpl implements ProducerService {

    @Autowired
    private KafkaConfig kafkaConfig;

    @Autowired
    private PgpUtils pgpUtils;

    @Value("${spring.kafka.topics.read-queue}")
    private String readQueueTopic;

    @Override
    public void sendMessage(String message) throws PGPException {
        String encryptedMessage = pgpUtils.encrypt(message);
        kafkaConfig.kafkaTemplate().send(readQueueTopic, encryptedMessage);
    }

}