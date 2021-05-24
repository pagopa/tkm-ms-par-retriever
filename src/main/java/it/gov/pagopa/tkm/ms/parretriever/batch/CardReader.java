package it.gov.pagopa.tkm.ms.parretriever.batch;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@StepScope
public class CardReader implements ItemReader</*TODO Card*/Object> {

    private String name;
    private AtomicInteger atomicInteger;

    @Override
    public Object read() {
        //Mappo le carte assegnate nel nostro DTO
        return null;
    }

}
