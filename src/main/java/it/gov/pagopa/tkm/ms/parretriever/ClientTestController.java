package it.gov.pagopa.tkm.ms.parretriever;

import it.gov.pagopa.tkm.ms.parretriever.client.amex.*;
import it.gov.pagopa.tkm.ms.parretriever.client.cards.*;
import it.gov.pagopa.tkm.ms.parretriever.client.cards.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.client.consent.*;
import it.gov.pagopa.tkm.ms.parretriever.client.consent.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.*;
import it.gov.pagopa.tkm.ms.parretriever.client.visa.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static it.gov.pagopa.tkm.ms.parretriever.constant.ApiParams.MAX_NUMBER_OF_CARDS;

//TODO: REMOVE (TEST CONTROLLER)
@RestController
@RequestMapping("/test")
public class ClientTestController {

    @Autowired
    private ConsentClient consentClient;

    @Autowired
    private ParlessCardsClient parlessCardsClient;

    @Autowired
    private MastercardClient mastercardClient;

    @Autowired
    private VisaClient visaClient;

    @Autowired
    private AmexClient amexClient;

    @GetMapping("/consent")
    public ConsentResponse getConsent(
            @RequestParam("taxcode") String taxCode,
            @RequestParam(value = "hpan", required = false) String hpan,
            @RequestParam(value = "services", required = false) String[] services) {
        return consentClient.getConsent(taxCode, hpan, services);
    }

    @GetMapping("/parless-cards")
    public List<ParlessCard> getParlessCards(
            @RequestParam(value = MAX_NUMBER_OF_CARDS) Integer maxNumberOfCards) {
        return parlessCardsClient.getParlessCards(maxNumberOfCards);
    }

    @GetMapping("/mastercard")
    public String getParMastercard(@RequestParam("pan") String pan) throws Exception {
        return mastercardClient.getPar(pan);
    }

    @GetMapping("/visa")
    public String getParVisa(@RequestParam("pan") String pan) throws Exception {
        return visaClient.getPar(pan);
    }

    @GetMapping("/amex")
    public String getParAmex(@RequestParam("pan") String pan) throws Exception {
        return amexClient.getPar(pan);
    }

}
