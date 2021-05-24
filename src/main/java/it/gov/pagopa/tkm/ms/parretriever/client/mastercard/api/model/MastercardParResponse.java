package it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.model;

import lombok.*;

@Data
@AllArgsConstructor
public class MastercardParResponse {

    private String responseId;

    private ParResponseEncryptedPayload encryptedPayload;

}

