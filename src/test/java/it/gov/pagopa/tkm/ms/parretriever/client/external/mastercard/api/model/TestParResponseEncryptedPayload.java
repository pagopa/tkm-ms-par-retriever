package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestParResponseEncryptedPayload {

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
