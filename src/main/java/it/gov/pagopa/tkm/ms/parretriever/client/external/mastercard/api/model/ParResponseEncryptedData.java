package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParResponseEncryptedData {

    private String paymentAccountReference;

    private String dataValidUntilTimestamp;

}

