package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model;

import lombok.*;

@Data
@AllArgsConstructor
public class ParResponseEncryptedData {

    private String paymentAccountReference;

    private String dataValidUntilTimestamp;

}

