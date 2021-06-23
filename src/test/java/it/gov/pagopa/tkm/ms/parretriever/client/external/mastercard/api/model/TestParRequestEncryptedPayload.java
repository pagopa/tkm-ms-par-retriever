package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestParRequestEncryptedPayload {

    private ParRequestEncryptedPayload parRequestEncryptedPayload;

    @Test
    void verifyProperties_setterGetter() {
        parRequestEncryptedPayload = new ParRequestEncryptedPayload(null);

        ParRequestEncryptedData parRequestEncryptedData = new ParRequestEncryptedData("754861235631",
                Instant.now().toString());
        parRequestEncryptedPayload.setEncryptedData(parRequestEncryptedData);
        assertEquals(parRequestEncryptedData, parRequestEncryptedPayload.getEncryptedData());
    }

}
