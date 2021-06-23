package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestMastercardParRequest {

    private MastercardParRequest request;

    @Test
    void verifyProperties_setterGetter() {
        request = new MastercardParRequest("requestId",
                new ParRequestEncryptedPayload(new ParRequestEncryptedData("accountNumber",
                        "dataValidUntilTimestamp")));

        String newRequestId = "0569664126";
        request.setRequestId(newRequestId);
        assertEquals(newRequestId, request.getRequestId());

        ParRequestEncryptedData parRequestEncryptedData = new ParRequestEncryptedData("3215589648565967",
                LocalDateTime.now().toString());
        ParRequestEncryptedPayload parRequestEncryptedPayload = new ParRequestEncryptedPayload(parRequestEncryptedData);
        request.setEncryptedPayload(parRequestEncryptedPayload);
        assertEquals(parRequestEncryptedPayload, request.getEncryptedPayload());
    }

    @Test
    void createInstance_complete() {
        request = new MastercardParRequest("requestId",
                new ParRequestEncryptedPayload(new ParRequestEncryptedData("accountNumber",
                        "dataValidUntilTimestamp")));

        MastercardParRequest mastercardParRequest = new MastercardParRequest("requestId",
                new ParRequestEncryptedPayload(new ParRequestEncryptedData("accountNumber",
                        "dataValidUntilTimestamp")));
        boolean equals = request.equals(mastercardParRequest);
        assertTrue(equals);

        assertTrue(request.hashCode()!=0);

        assertNotNull(request.toString());

        assertTrue(request.canEqual(mastercardParRequest));
    }

}
