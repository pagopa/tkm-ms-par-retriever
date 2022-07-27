package it.gov.pagopa.tkm.ms.parretriever.service.impl;

import it.gov.pagopa.tkm.ms.parretriever.service.*;
import it.gov.pagopa.tkm.service.*;
import lombok.extern.log4j.*;
import org.bouncycastle.openpgp.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.kafka.core.*;
import org.springframework.stereotype.*;

@Service
@Log4j2
public final class ProducerServiceImpl implements ProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topics.read-queue}")
    private String readQueueTopic;

    @Value("${keyvault.readQueuePubPgpKey}")
    private String pgpPublicKey;

    @Override
    public void sendMessage(String message) throws PGPException {
        kafkaTemplate.send(readQueueTopic, PgpStaticUtils.encrypt(message, pgpPublicKey));
    }

}