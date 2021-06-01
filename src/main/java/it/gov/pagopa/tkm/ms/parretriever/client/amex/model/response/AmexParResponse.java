package it.gov.pagopa.tkm.ms.parretriever.client.amex.model.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmexParResponse {

    private String input;

    private String par;

    private Boolean success;

}
