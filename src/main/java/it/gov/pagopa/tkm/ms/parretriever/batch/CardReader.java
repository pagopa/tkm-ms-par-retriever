package it.gov.pagopa.tkm.ms.parretriever.batch;

import org.springframework.batch.core.annotation.BeforeRead;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@StepScope
public class CardReader implements ItemReader</*TODO Card*/Object> {

    private final ArrayDeque</*TODO Card*/Object> coda = new ArrayDeque<>();

    public CardReader(List<Object> lista) {
        coda.addAll(lista);
    }

    @Override
    public /*TODO Card*/Object read() {
        //Mappo le carte assegnate nel nostro DTO
        return coda.pop();
    }

}
