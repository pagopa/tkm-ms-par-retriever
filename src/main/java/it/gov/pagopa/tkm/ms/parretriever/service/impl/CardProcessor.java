package it.gov.pagopa.tkm.ms.parretriever.service.impl;

import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.*;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.consentmanager.*;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.consentmanager.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.constant.CircuitEnum;
import it.gov.pagopa.tkm.ms.parretriever.service.CryptoService;
import org.jetbrains.annotations.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.*;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static it.gov.pagopa.tkm.ms.parretriever.client.internal.consentmanager.model.response.ConsentRequestEnum.Allow;

@Component
@StepScope
public class CardProcessor implements ItemProcessor<ParlessCard, ParlessCard> {

    @Value("${circuit-activation.amex}")
    private String isAmexActive;

    @Value("${circuit-activation.mastercard}")
    private String isMastercardActive;

    @Value("${circuit-activation.visa}")
    private String isVisaActive;

    @Autowired
    private ParlessCardsClient parlessCardsClient;

    @Autowired
    private ConsentClient consentClient;

    @Autowired
    private CryptoService cryptoService;

    @Override
    public ParlessCard process(@NotNull ParlessCard parlessCard) {
        CircuitEnum circuit = parlessCard.getCircuit();
        return circuit != null && isCircuitActive(circuit) ? decrypt(parlessCard) : null;
    }

    private boolean isCircuitActive(CircuitEnum circuit) {
        switch (circuit) {
            case MASTERCARD:
                return isMastercardActive.equals("true");
            case VISA:
            case VISA_ELECTRON:
            case VPAY:
                return isVisaActive.equals("true");
            case AMEX:
                return isAmexActive.equals("true");
            default:
                return false;
        }
    }

    private ParlessCard decrypt(ParlessCard parlessCard) {
        parlessCard.setPan(cryptoService.decrypt(parlessCard.getPan()));
        parlessCard.getTokens().forEach(parlessCardToken ->
                parlessCardToken.setToken(cryptoService.decrypt(parlessCardToken.getToken())));
        return parlessCard;
    }

}
