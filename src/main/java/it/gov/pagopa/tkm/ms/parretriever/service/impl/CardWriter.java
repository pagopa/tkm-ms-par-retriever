package it.gov.pagopa.tkm.ms.parretriever.service.impl;

import com.fasterxml.jackson.databind.*;
import com.google.common.util.concurrent.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.amex.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.visa.*;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.constant.*;
import it.gov.pagopa.tkm.ms.parretriever.model.topic.*;
import it.gov.pagopa.tkm.util.*;
import lombok.extern.log4j.*;
import org.apache.commons.collections.*;
import org.apache.commons.lang3.*;
import org.jetbrains.annotations.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
@StepScope
@Log4j2
public class CardWriter implements ItemWriter<ParlessCard> {
    public CardWriter(List<ParlessCard> list, Double ratelimit) {
        this.rateLimit = ratelimit;
    }

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

    private Double rateLimit;

    @Override
    public void write(@NotNull List<? extends ParlessCard> list) throws Exception {
        log.info("START write - number of parless cards " + list.size());
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

    private String getParFromCircuitLog(CircuitEnum circuit, String pan, String hpan, boolean isToken) throws Exception {
        String hpanLabel = isToken ? "htoken" : "hpan";
        log.info("START Retrieve PAR for " + hpanLabel + " " + ObfuscationUtils.obfuscateHpan(hpan) + " from " + circuit);
        String par = getParFromCircuit(circuit,pan);
        log.info("END Retrieve PAR for " + hpanLabel + " " + ObfuscationUtils.obfuscateHpan(hpan) + " from " + circuit);
        return par;
    }


    private void writeCardsOnKafkaQueue(List<? extends ParlessCard> parlessCardResponseList) throws Exception {
      RateLimiter rateLimiter = RateLimiter.create(rateLimit);
        for (ParlessCard parlessCard : parlessCardResponseList) {

            rateLimiter.acquire(1);

            CircuitEnum circuit = parlessCard.getCircuit();
            if (!checkParRetrieveEnabledAndRateLimitByCircuit(circuit)) continue;

            String pan = parlessCard.getPan();
            String par = null;
            String hpan = parlessCard.getHpan();
            Set<ParlessCardToken> tokens = parlessCard.getTokens();

            if (StringUtils.isNotEmpty(pan)) {
                par = getParFromCircuitLog(circuit, pan, hpan, false);
            } else if (CollectionUtils.isNotEmpty(tokens)) {
                // to retrieve par for a token linked only to a "fake" card
                ParlessCardToken cardToken = parlessCard.getTokens().stream().findFirst().get();
                String tokenPan = cardToken.getToken();
                String htoken = cardToken.getHtoken();
                par = getParFromCircuitLog(circuit, tokenPan, htoken, true);
            } else {
                log.info("Pan is null and tokens is empty, not retrieving PAR");
            }

            if (par != null) {
                log.trace("Retrieved PAR. Writing card into the queue");
                producerService.sendMessage(mapper.writeValueAsString(new ReadQueue(pan,
                        hpan, par, circuit, tokens)));
            } else {
                log.trace("PAR not found for card");
            }
        }
    }

    private boolean checkParRetrieveEnabledAndRateLimitByCircuit(CircuitEnum circuit) {
        switch (circuit) {
            case AMEX:
                return amexParRetrieveEnabled;
            case MASTERCARD:
                return mastercardParRetrieveEnabled;
            case VISA:
            case VISA_ELECTRON:
            case VPAY:
                return visaParRetrieveEnabled;
            default:
                return false;
        }
    }

}
