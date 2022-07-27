package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.junit.jupiter.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class TestParResponseEncryptedPayload {

    private ParResponseEncryptedPayload parResponseEncryptedPayload;

    @Test
    void createInstance_complete() {
        parResponseEncryptedPayload = new ParResponseEncryptedPayload();
        parResponseEncryptedPayload.setEncryptedData(new ParResponseEncryptedData());

        ParResponseEncryptedPayload payload = new ParResponseEncryptedPayload(new ParResponseEncryptedData());
        boolean equals = parResponseEncryptedPayload.equals(payload);
        assertTrue(equals);
    }

}
