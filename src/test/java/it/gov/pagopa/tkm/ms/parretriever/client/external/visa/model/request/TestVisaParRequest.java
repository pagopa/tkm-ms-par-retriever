package it.gov.pagopa.tkm.ms.parretriever.client.external.visa.model.request;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.junit.jupiter.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class TestVisaParRequest {

    private VisaParRequest visaParRequest;

    @Test
    void checkProperties_setterGetter() {
        visaParRequest = new VisaParRequest(null, null, null);

        visaParRequest.setClientId("clientId");
        assertEquals("clientId", visaParRequest.getClientId());

        visaParRequest.setCorrelatnId("correlatnId");
        assertEquals("correlatnId", visaParRequest.getCorrelatnId());

        visaParRequest.setPrimaryAccount("primaryAccount");
        assertEquals("primaryAccount", visaParRequest.getPrimaryAccount());
    }

    @Test
    void createInstance_complete() {
        visaParRequest = new VisaParRequest("clientId", "correlatnId", "primaryAccount");
        VisaParRequest request = new VisaParRequest("clientId", "correlatnId", "primaryAccount");

        boolean equals = visaParRequest.equals(request);
        assertTrue(equals);

        assertTrue(visaParRequest.canEqual(request));

        assertTrue(visaParRequest.hashCode() != 0);

        assertNotNull(visaParRequest.toString());
    }

}
