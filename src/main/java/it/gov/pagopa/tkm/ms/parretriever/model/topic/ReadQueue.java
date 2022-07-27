package it.gov.pagopa.tkm.ms.parretriever.model.topic;

import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.constant.*;
import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadQueue {

    private String pan;
    private String hpan;
    private String par;
    private CircuitEnum circuit;
    private Set<ParlessCardToken> tokens;

}
