package it.gov.pagopa.tkm.ms.parretriever.client.external.visa.model.response;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.junit.jupiter.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class TestVisaParEncryptedResponse {

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
