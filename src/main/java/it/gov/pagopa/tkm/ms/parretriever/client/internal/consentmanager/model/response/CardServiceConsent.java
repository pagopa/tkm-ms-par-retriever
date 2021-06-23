package it.gov.pagopa.tkm.ms.parretriever.client.internal.consentmanager.model.response;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardServiceConsent {

    private String hpan;

    private Set<ServiceConsent> serviceConsents;

}
