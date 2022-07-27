package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.junit.jupiter.*;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class TestParRequestEncryptedPayload {

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
