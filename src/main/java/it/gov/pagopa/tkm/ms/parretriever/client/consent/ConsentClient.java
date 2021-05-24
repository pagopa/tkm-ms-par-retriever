package it.gov.pagopa.tkm.ms.parretriever.client.consent;

import it.gov.pagopa.tkm.ms.parretriever.client.consent.model.response.*;
import org.springframework.cloud.openfeign.*;
import org.springframework.web.bind.annotation.*;

import static it.gov.pagopa.tkm.ms.parretriever.constant.ApiParams.*;

@FeignClient(value = "consent", url = "${tkm-ms-urls.consent-manager}")
public interface ConsentClient {

    @GetMapping("/consent")
    GetConsentResponse getConsent(
            @RequestHeader(TAX_CODE_HEADER) String taxCode,
            @RequestParam(value = HPAN_QUERY_PARAM, required = false) String hpan,
            @RequestParam(value = SERVICES_QUERY_PARAM, required = false) String[] services
    );

}
