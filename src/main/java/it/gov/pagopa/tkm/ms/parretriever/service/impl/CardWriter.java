package it.gov.pagopa.tkm.ms.parretriever.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import it.gov.pagopa.tkm.ms.parretriever.client.external.amex.AmexClient;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.MastercardClient;
import it.gov.pagopa.tkm.ms.parretriever.client.external.visa.VisaClient;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.model.response.ParlessCard;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.model.response.ParlessCardToken;
import it.gov.pagopa.tkm.ms.parretriever.constant.CircuitEnum;
import it.gov.pagopa.tkm.ms.parretriever.model.topic.ReadQueue;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

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
        log.info("START Retrieve PAR for " + hpanLabel + " " + hpan + " from " + circuit);
        String par = getParFromCircuit(circuit,pan);
        log.info("END Retrieve PAR for " + hpanLabel + " " + hpan + " from " + circuit + " - " + par);
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
