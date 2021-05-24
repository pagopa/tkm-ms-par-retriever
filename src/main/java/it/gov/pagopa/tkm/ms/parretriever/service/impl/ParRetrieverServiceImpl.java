package it.gov.pagopa.tkm.ms.parretriever.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.gov.pagopa.tkm.ms.parretriever.service.*;
import org.bouncycastle.openpgp.PGPException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.*;

@Service
public class ParRetrieverServiceImpl implements ParRetrieverService {

    ParSenderService parSenderService;

    @Override
    @Scheduled(cron = "${core.parRetrieverService.getPar.scheduler}")
    public void getPar() throws PGPException, JsonProcessingException {
        parSenderService.addRecordToKafkaQueue();
    }

}
