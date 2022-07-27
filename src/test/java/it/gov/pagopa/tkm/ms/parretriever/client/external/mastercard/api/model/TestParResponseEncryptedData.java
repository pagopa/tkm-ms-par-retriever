package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.junit.jupiter.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class TestParResponseEncryptedData {

    private ParResponseEncryptedData parResponseEncryptedData;

    @Test
    void createInstance_complete() {
        String now = "2022-04-19";

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
