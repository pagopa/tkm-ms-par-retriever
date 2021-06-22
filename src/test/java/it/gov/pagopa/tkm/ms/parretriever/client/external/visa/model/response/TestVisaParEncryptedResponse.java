package it.gov.pagopa.tkm.ms.parretriever.client.external.visa.model.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestVisaParEncryptedResponse {

    private VisaParEncryptedResponse visaParEncryptedResponse = new VisaParEncryptedResponse("encData");

    @Test
    void createInstance_complete() {
        VisaParEncryptedResponse response = new VisaParEncryptedResponse("encData");

        boolean equals = visaParEncryptedResponse.equals(response);
        assertTrue(equals);

        assertTrue(visaParEncryptedResponse.canEqual(response));

        assertTrue(visaParEncryptedResponse.hashCode() != 0);

        assertNotNull(visaParEncryptedResponse.toString());
    }

}
