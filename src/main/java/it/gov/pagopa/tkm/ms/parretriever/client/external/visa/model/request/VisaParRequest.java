package it.gov.pagopa.tkm.ms.parretriever.client.external.visa.model.request;

import lombok.*;

@Data
@AllArgsConstructor
public class VisaParRequest {

    private String clientId;

    private String correlatnId;

    private String primaryAccount;

}
