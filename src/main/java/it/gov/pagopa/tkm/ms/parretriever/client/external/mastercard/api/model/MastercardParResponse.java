package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MastercardParResponse {

    private String responseId;

    private ParResponseEncryptedPayload encryptedPayload;

}

