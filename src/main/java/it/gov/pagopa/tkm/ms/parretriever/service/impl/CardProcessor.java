package it.gov.pagopa.tkm.ms.parretriever.service.impl;

import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.*;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.constant.*;
import it.gov.pagopa.tkm.ms.parretriever.service.*;
import lombok.extern.log4j.*;
import org.jetbrains.annotations.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Component
@StepScope
@Log4j2
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
    private CryptoService cryptoService;

    @Override
    public ParlessCard process(@NotNull ParlessCard parlessCard) {
        log.trace(parlessCard);
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
