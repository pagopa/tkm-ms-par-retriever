package it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.model;

import lombok.*;

@Data
@AllArgsConstructor
public class ParRequestEncryptedData {

    private String accountNumber;

    private String dataValidUntilTimestamp;

}
