package it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.model.response;

import it.gov.pagopa.tkm.ms.parretriever.constant.*;
import lombok.*;

import java.io.Serializable;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParlessCard implements Serializable {

    private String taxCode;

    private String pan;

    private String hpan;

    private Set<String> tokens;

    private CircuitEnum circuit;

}
