package it.gov.pagopa.tkm.ms.parretriever.batch;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@StepScope
public class CardWriter implements ItemWriter<String> {

    @Override
    public void write(List<? extends String> list) {
        System.out.println("Scrivo carte su coda");
        //Scrittura sulla coda
    }

}
