package it.gov.pagopa.tkm.ms.parretriever.service.impl;

import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.model.response.*;
import org.apache.commons.collections.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.item.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
@StepScope
public class CardReader implements ItemReader<ParlessCard> {

    private final ArrayDeque<ParlessCard> queue = new ArrayDeque<>();

    public CardReader(List<ParlessCard> list) {
        if (list != null) {
            queue.addAll(list);
        }
    }

    @Override
    public ParlessCard read() {
        return CollectionUtils.isNotEmpty(queue) ? queue.pop() : null;
    }

}
