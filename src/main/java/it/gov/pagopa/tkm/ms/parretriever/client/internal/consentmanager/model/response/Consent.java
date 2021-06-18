package it.gov.pagopa.tkm.ms.parretriever.client.internal.consentmanager.model.response;

import lombok.*;
import lombok.Builder;

import javax.validation.constraints.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consent {

    @NotNull
    private ConsentRequestEnum consent;

    @Size(min = 64, max = 64)
    private String hpan;

    private Set<ServiceEnum> services;

    public boolean isPartial() {
        return hpan != null;
    }

}
