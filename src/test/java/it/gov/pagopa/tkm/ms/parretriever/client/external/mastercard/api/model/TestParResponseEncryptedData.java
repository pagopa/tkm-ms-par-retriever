package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestParResponseEncryptedData {

    private ParResponseEncryptedData parResponseEncryptedData;

    @Test
    void createInstance_complete() {
        String now = LocalDateTime.now().toString();

        parResponseEncryptedData = new ParResponseEncryptedData();
        parResponseEncryptedData.setPaymentAccountReference("111111111111111");
        parResponseEncryptedData.setDataValidUntilTimestamp(now);

        ParResponseEncryptedData data = new ParResponseEncryptedData("111111111111111", now);
        boolean equals = parResponseEncryptedData.equals(data);
        assertTrue(equals);

        assertTrue(parResponseEncryptedData.hashCode() != 0);

        assertNotNull(parResponseEncryptedData.toString());

        assertTrue(parResponseEncryptedData.canEqual(data));
    }

}
