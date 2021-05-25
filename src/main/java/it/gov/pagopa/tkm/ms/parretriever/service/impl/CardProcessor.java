package it.gov.pagopa.tkm.ms.parretriever.service.impl;

import it.gov.pagopa.tkm.ms.parretriever.client.cards.*;
import it.gov.pagopa.tkm.ms.parretriever.client.cards.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.client.consent.*;
import it.gov.pagopa.tkm.ms.parretriever.client.consent.model.response.*;
import org.jetbrains.annotations.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

import static it.gov.pagopa.tkm.ms.parretriever.client.consent.model.response.ConsentRequestEnum.Allow;

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

    private GetConsentResponse getConsent(ParlessCard parlessCard) {
        return consentClient.getConsent(parlessCard.getTaxCode(), parlessCard.getHpan(), null);
    }

    private boolean getConsentForCard(ParlessCard parlessCard) {
        String hpan = parlessCard.getHpan();
        GetConsentResponse consent = getConsent(parlessCard);
        switch (consent.getConsent()) {
            case Allow:
                return true;
            case Partial:
                return consent.getDetails().stream().anyMatch(c ->
                        c.getConsent().equals(Allow) && c.getHpan() != null && c.getHpan().equals(hpan));
            case Deny:
            default:
                return false;
        }
    }

}
