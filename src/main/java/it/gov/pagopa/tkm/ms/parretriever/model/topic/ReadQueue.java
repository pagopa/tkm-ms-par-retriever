package it.gov.pagopa.tkm.ms.parretriever.model.topic;

import it.gov.pagopa.tkm.ms.parretriever.constant.CircuitEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadQueue {

    private String taxCode;
    private String pan;
    private String hpan;
    private String par;
    private CircuitEnum circuit;
    private List<Token> tokens;

}
