package it.gov.pagopa.tkm.ms.parretriever.service.impl;

import it.gov.pagopa.tkm.ms.parretriever.service.*;
import it.gov.pagopa.tkm.service.*;
import lombok.extern.log4j.Log4j2;
import org.bouncycastle.openpgp.PGPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static it.gov.pagopa.tkm.ms.parretriever.constant.Constants.TKM_READ_TOKEN_PAR_PAN_TOPIC;

@Service
@Log4j2
public final class ProducerServiceImpl implements ProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private PgpUtils pgpUtils;

    @Override
    public void sendMessage(String message) throws PGPException {
        String encryptedMessage = pgpUtils.encrypt(message);
        kafkaTemplate.send(TKM_READ_TOKEN_PAR_PAN_TOPIC, encryptedMessage);
    }

}