package it.gov.pagopa.tkm.ms.parretriever.client.external.visa.model.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisaParDecryptedResponse {

    private String paymentAccountReference;

    private String paymentAccountReferenceCreationDate;

    private String primaryAccount;

}
