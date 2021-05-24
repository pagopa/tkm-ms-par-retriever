package it.gov.pagopa.tkm.ms.parretriever.client.visa.model.response;

import lombok.*;

@Data
public class VisaParDecryptedResponse {

    private String paymentAccountReference;

    private String paymentAccountReferenceCreationDate;

    private String primaryAccount;

}
