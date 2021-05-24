package it.gov.pagopa.tkm.ms.parretriever.service.impl;

import it.gov.pagopa.tkm.ms.parretriever.crypto.PgpUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static it.gov.pagopa.tkm.ms.parretriever.constant.Constants.TKM_READ_TOKEN_PAR_PAN_TOPIC;

@Service
@Log4j2
public final class ConsumerService {

    @Autowired
    private PgpUtils pgpUtils;

    @KafkaListener(topics = TKM_READ_TOKEN_PAR_PAN_TOPIC)
    public void consume(String message) throws Exception {
        String decryptedMessage = pgpUtils.decrypt(message);
        log.info("Consumed message: " + decryptedMessage);
    }

}