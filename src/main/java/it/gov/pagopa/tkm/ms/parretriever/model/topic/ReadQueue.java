package it.gov.pagopa.tkm.ms.parretriever.model.topic;

import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.model.response.ParlessCardToken;
import it.gov.pagopa.tkm.ms.parretriever.constant.CircuitEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

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
