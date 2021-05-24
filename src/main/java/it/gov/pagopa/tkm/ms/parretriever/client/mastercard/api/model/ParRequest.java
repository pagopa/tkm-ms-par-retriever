package it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.model;

import lombok.*;

@Data
@AllArgsConstructor
public class ParRequest {

  private String requestId;

  private ParRequestEncryptedPayload encryptedPayload;

}

