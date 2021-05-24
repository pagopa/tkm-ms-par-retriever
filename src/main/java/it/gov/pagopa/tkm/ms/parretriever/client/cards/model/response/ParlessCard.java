package it.gov.pagopa.tkm.ms.parretriever.client.cards.model.response;

import it.gov.pagopa.tkm.ms.parretriever.constant.*;
import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParlessCard {

    private String taxCode;

    private String pan;

    private String hpan;

    private Set<String> tokens;

    private CircuitEnum circuit;

}
