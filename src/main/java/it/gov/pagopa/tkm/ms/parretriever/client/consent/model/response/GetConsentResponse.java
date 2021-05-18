package it.gov.pagopa.tkm.ms.parretriever.client.consent.model.response;

import lombok.*;
import lombok.experimental.*;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GetConsentResponse {

    private ConsentEntityEnum consent;

    private List<ConsentResponse> details;

}
