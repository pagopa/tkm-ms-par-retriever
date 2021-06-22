package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestMastercardParResponse {

    private MastercardParResponse response;

    @Test
    void createInstance_complete() {
        response = new MastercardParResponse();
        response.setResponseId("responseId");
        response.setEncryptedPayload(new ParResponseEncryptedPayload());

        MastercardParResponse mastercardParResponse = new MastercardParResponse("responseId",
                new ParResponseEncryptedPayload());
        boolean equals = response.equals(mastercardParResponse);
        assertTrue(equals);

        assertTrue(response.hashCode()!=0);

        assertNotNull(response.toString());

        assertTrue(response.canEqual(mastercardParResponse));
    }

}
