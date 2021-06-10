package it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager;

import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.model.response.*;
import org.springframework.cloud.openfeign.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static it.gov.pagopa.tkm.ms.parretriever.constant.ApiParams.*;

@FeignClient(value = "parless-cards", url = "${client-urls.card-manager}")
public interface ParlessCardsClient {

    @GetMapping("/parless-cards")
    List<ParlessCard> getParlessCards(
            @RequestParam(value = MAX_NUMBER_OF_CARDS) Integer maxNumberOfCards
    );

}
