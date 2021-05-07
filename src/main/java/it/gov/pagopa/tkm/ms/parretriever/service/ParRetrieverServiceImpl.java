package it.gov.pagopa.tkm.ms.parretriever.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Configuration
@EnableScheduling
public class ParRetrieverServiceImpl implements ParRetrieverService {

    @Override
    @Scheduled(cron = "${core.parRetrieverService.getBinRange.scheduler}")
    public void getBinRange() {
        System.out.println("ciao");
    }

}
