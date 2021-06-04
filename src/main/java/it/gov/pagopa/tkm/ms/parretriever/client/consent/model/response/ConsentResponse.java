package it.gov.pagopa.tkm.ms.parretriever.client.consent.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ConsentResponse {

    private ConsentEntityEnum consent;

    private String lastUpdateDate;

    private Set<CardServiceConsent> details;

}
