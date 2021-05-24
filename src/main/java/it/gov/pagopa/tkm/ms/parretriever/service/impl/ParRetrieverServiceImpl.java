package it.gov.pagopa.tkm.ms.parretriever.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.gov.pagopa.tkm.ms.parretriever.service.*;
import org.bouncycastle.openpgp.PGPException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Configuration
@EnableScheduling
public class ParRetrieverServiceImpl implements ParRetrieverService {


    ParSenderService parSenderService;

    @Override
    @Scheduled(cron = "${core.parRetrieverService.getBinRange.scheduler}")
    public void getBinRange() throws PGPException, JsonProcessingException {
        parSenderService.addRecordToKafkaQueue();
    }




}
