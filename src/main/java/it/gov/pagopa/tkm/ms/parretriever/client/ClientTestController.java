package it.gov.pagopa.tkm.ms.parretriever.client;

import it.gov.pagopa.tkm.ms.parretriever.client.consent.*;
import it.gov.pagopa.tkm.ms.parretriever.client.consent.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.*;
import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.model.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

//TODO: REMOVE (TEST CONTROLLER)
@RestController
@RequestMapping("/test")
public class ClientTestController {

    @Autowired
    private ConsentClient consentClient;

    @Autowired
    private MastercardParClient mastercardParClient;

    @GetMapping("/consent")
    public GetConsentResponse getConsent(
            @RequestParam("taxcode") String taxCode,
            @RequestParam(value = "hpan", required = false) String hpan,
            @RequestParam(value = "services", required = false) String[] services) {
        return consentClient.getConsent(taxCode, hpan, services);
    }

    @GetMapping("/mastercard")
    public ParResponse getPar(@RequestParam("acc") String account) throws Exception {
        return mastercardParClient.getPar(account);
    }

}
