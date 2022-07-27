package it.gov.pagopa.tkm.ms.parretriever.client.external.visa.model.response;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.junit.jupiter.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class TestVisaParDecryptedResponse {

    private VisaParDecryptedResponse visaParDecryptedResponse = new VisaParDecryptedResponse(
            "paymentAccountReference",
            "paymentAccountReferenceCreationDate", "primaryAccount");

    @Test
    void checkProperties_getter() {
        assertEquals("paymentAccountReferenceCreationDate",
                visaParDecryptedResponse.getPaymentAccountReferenceCreationDate());
        assertEquals("primaryAccount", visaParDecryptedResponse.getPrimaryAccount());
    }

    @Test
    void createInstance_complete() {
        VisaParDecryptedResponse response = new VisaParDecryptedResponse(
                "paymentAccountReference",
                "paymentAccountReferenceCreationDate", "primaryAccount");

        boolean equals = visaParDecryptedResponse.equals(response);
        assertTrue(equals);

        assertTrue(visaParDecryptedResponse.canEqual(response));

        assertTrue(visaParDecryptedResponse.hashCode() != 0);

        assertNotNull(visaParDecryptedResponse.toString());
    }

}
