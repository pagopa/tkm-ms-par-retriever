package it.gov.pagopa.tkm.ms.parretriever.service.impl;

import com.fasterxml.jackson.databind.*;
import com.google.common.util.concurrent.RateLimiter;
import it.gov.pagopa.tkm.ms.parretriever.client.amex.*;
import it.gov.pagopa.tkm.ms.parretriever.client.cards.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.*;
import it.gov.pagopa.tkm.ms.parretriever.client.visa.*;
import it.gov.pagopa.tkm.ms.parretriever.constant.*;
import it.gov.pagopa.tkm.ms.parretriever.model.topic.*;
import org.jetbrains.annotations.*;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

import java.time.Duration;
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

    @Value("${batch-execution.amex-par-retrieve-enabled}")
    private Boolean amexParRetrieveEnabled;

    @Value("${batch-execution.mastercard-par-retrieve-enabled}")
    private Boolean mastercardParRetrieveEnabled;

    @Value("${batch-execution.visa-par-retrieve-enabled}")
    private Boolean visaParRetrieveEnabled;

    @Value("${batch-execution.amex-max-api-client-call-rate}")
    private Double amexMaxApiClientCallRate;

    @Value("${batch-execution.mastercard-max-api-client-call-rate}")
    private Double mastercardMaxApiClientCallRate;

    @Value("${batch-execution.visa-max-api-client-call-rate}")
    private Double visaMaxApiClientCallRate;

    RateLimiter amexRateLimiter = RateLimiter.create(amexMaxApiClientCallRate);
    RateLimiter mastercardRateLimiter = RateLimiter.create(mastercardMaxApiClientCallRate);
    RateLimiter visaRateLimiter = RateLimiter.create(visaMaxApiClientCallRate);

    private StepExecution stepExecution;

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
        ExecutionContext stepContext = this.stepExecution.getExecutionContext();
        RateLimiter rateLimiter = (RateLimiter)stepContext.get("rateLimiter");
        rateLimiter.acquire(1);

        for (ParlessCard parlessCard : parlessCardResponseList) {
            CircuitEnum circuit = parlessCard.getCircuit();
            if (!checkParRetrieveEnabledAndRateLimitByCircuit(circuit)) continue;

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

    private boolean checkParRetrieveEnabledAndRateLimitByCircuit(CircuitEnum circuit) {
        switch (circuit) {
            case AMEX:
                return amexParRetrieveEnabled && acquireLimiterSlot(amexRateLimiter);
            case MASTERCARD:
                return mastercardParRetrieveEnabled && acquireLimiterSlot(mastercardRateLimiter);
            case VISA:
            case VISA_ELECTRON:
            case VPAY:
                return visaParRetrieveEnabled && acquireLimiterSlot(visaRateLimiter);
            default:
                return false;
        }

    }

    private Boolean acquireLimiterSlot(RateLimiter limiter){
        limiter.acquire(1);
        return true;
  }

}
