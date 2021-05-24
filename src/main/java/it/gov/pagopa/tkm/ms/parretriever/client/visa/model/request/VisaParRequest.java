package it.gov.pagopa.tkm.ms.parretriever.client.visa.model.request;

import lombok.*;

@Data
@AllArgsConstructor
public class VisaParRequest {

    private String clientId;

    private String correlatnId;

    private String primaryAccount;

}
