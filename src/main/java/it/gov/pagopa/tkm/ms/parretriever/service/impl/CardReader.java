package it.gov.pagopa.tkm.ms.parretriever.service.impl;

import it.gov.pagopa.tkm.ms.parretriever.client.cards.model.response.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.List;

@Component
@StepScope
public class CardReader implements ItemReader<ParlessCard> {

    private final ArrayDeque<ParlessCard> queue = new ArrayDeque<>();

    public CardReader(List<ParlessCard> list) {
        queue.addAll(list);
    }

    @Override
    public ParlessCard read() {
        return queue.pop();
    }

}
