package it.gov.pagopa.tkm.ms.parretriever.batch;

import com.fasterxml.jackson.databind.*;
import it.gov.pagopa.tkm.ms.parretriever.client.amex.*;
import it.gov.pagopa.tkm.ms.parretriever.client.cards.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.*;
import it.gov.pagopa.tkm.ms.parretriever.client.visa.*;
import it.gov.pagopa.tkm.ms.parretriever.constant.*;
import it.gov.pagopa.tkm.ms.parretriever.model.topic.*;
import it.gov.pagopa.tkm.ms.parretriever.service.impl.*;
import org.jetbrains.annotations.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.*;

@Component
@StepScope
public class CardWriter implements ItemWriter<ParlessCard> {

    @Autowired
    private ProducerServiceImpl producerService;

    @Autowired
    private VisaClient visaClient;

    @Autowired
    private MastercardClient mastercardClient;

    @Autowired
    private AmexClient amexClient;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void write(@NotNull List<? extends ParlessCard> list) throws Exception {
        writeCardsOnKafkaQueue(list);
    }

    private String getParFromCircuit(CircuitEnum circuit, String pan) throws Exception {
        switch (circuit) {
            //case DINERS:
            //case MAESTRO:
            case MASTERCARD:
                return mastercardClient.getPar(pan);
            case VISA:
            case VISA_ELECTRON:
            case VPAY:
                return visaClient.getPar(pan);
            case AMEX:
                return amexClient.getPar(pan);
            default:
                return null;
        }
    }

    private void writeCardsOnKafkaQueue(List<? extends ParlessCard> parlessCardResponseList) throws Exception {
        for (ParlessCard parlessCard : parlessCardResponseList) {
            CircuitEnum circuit = parlessCard.getCircuit();
            String par = getParFromCircuit(circuit, parlessCard.getPan());
            ReadQueue readQueue = new ReadQueue(parlessCard.getTaxCode(),
                    parlessCard.getPan(),
                    parlessCard.getHpan(),
                    par,
                    circuit,
                    getTokenListFromStringSet(parlessCard.getTokens()));
            producerService.sendMessage(mapper.writeValueAsString(readQueue));
        }
    }

    private List<Token> getTokenListFromStringSet(Set<String> tokens) {
        return tokens.stream().map(z -> new Token(z, null)).collect(Collectors.toList());
    }

}
