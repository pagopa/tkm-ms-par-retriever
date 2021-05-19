package it.gov.pagopa.tkm.ms.parretriever.client.cards.model.response;

import it.gov.pagopa.tkm.ms.parretriever.constant.*;
import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParlessCardResponse {

    private String taxCode;

    private String pan;

    private Set<String> tokens;

    private CircuitEnum circuit;

}
