package it.gov.pagopa.tkm.ms.parretriever.service.impl;

import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.*;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.consentmanager.*;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.consentmanager.model.response.*;
import org.jetbrains.annotations.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.*;

import static it.gov.pagopa.tkm.ms.parretriever.client.internal.consentmanager.model.response.ConsentRequestEnum.Allow;

@Component
@StepScope
public class CardProcessor implements ItemProcessor<ParlessCard, ParlessCard> {

    @Autowired
    private ParlessCardsClient parlessCardsClient;

    @Autowired
    private ConsentClient consentClient;

    @Override
    public ParlessCard process(@NotNull ParlessCard parlessCard) {
        return getConsentForCard(parlessCard) ? parlessCard : null;
    }

    private ConsentResponse getConsent(ParlessCard parlessCard) {
        return consentClient.getConsent(parlessCard.getTaxCode(), parlessCard.getHpan(), null);
    }

    private boolean getConsentForCard(ParlessCard parlessCard) {
        ConsentResponse consent = getConsent(parlessCard);
        switch (consent.getConsent()) {
            case Allow:
                return true;
            case Partial:
                CardServiceConsent cardServiceConsent = CollectionUtils.firstElement(consent.getDetails());
                if (cardServiceConsent != null) {
                    return cardServiceConsent.getServiceConsents().stream().anyMatch(c -> c.getConsent().equals(Allow));
                }
            case Deny:
            default:
                return false;
        }
    }

}
