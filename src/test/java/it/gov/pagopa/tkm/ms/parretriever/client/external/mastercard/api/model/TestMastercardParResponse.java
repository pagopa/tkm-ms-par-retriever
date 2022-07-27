package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.junit.jupiter.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class TestMastercardParResponse {

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
