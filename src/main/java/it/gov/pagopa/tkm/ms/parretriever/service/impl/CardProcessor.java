package it.gov.pagopa.tkm.ms.parretriever.service.impl;

import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.*;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.consentmanager.*;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.consentmanager.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.service.CryptoService;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class CardProcessor implements ItemProcessor<ParlessCard, ParlessCard> {

    @Autowired
    private ParlessCardsClient parlessCardsClient;

    @Autowired
    private ConsentClient consentClient;

    @Autowired
    private CryptoService cryptoService;

    @Override
    public ParlessCard process(@NotNull ParlessCard parlessCard) {
        log.info(parlessCard.getCircuit());
        return parlessCard.getCircuit() != null ? decrypt(parlessCard) : null;
    }

    private ParlessCard decrypt(ParlessCard parlessCard) {
        parlessCard.setPan(cryptoService.decrypt(parlessCard.getPan()));
        parlessCard.getTokens().forEach(parlessCardToken ->
                parlessCardToken.setToken(cryptoService.decrypt(parlessCardToken.getToken())));
        return parlessCard;
    }

}
