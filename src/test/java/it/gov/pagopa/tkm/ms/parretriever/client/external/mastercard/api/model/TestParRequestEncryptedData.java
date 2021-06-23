package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestParRequestEncryptedData {

    private ParRequestEncryptedData parRequestEncryptedData;

    @Test
    void verifyProperties_setterGetter() {
        parRequestEncryptedData = new ParRequestEncryptedData(null, null);

        String accountNumber = "652541239587";
        parRequestEncryptedData.setAccountNumber(accountNumber);
        assertEquals(accountNumber, parRequestEncryptedData.getAccountNumber());

        String dataValidUntilTimestamp = Instant.now().toString();
        parRequestEncryptedData.setDataValidUntilTimestamp(dataValidUntilTimestamp);
        assertEquals(dataValidUntilTimestamp, parRequestEncryptedData.getDataValidUntilTimestamp());
    }

}
