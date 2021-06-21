package it.gov.pagopa.tkm.ms.parretriever.client.internal.consentmanager.model.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestServiceConsent {

    private ServiceConsent serviceConsent;

    @Test
    void verifyProperties_setterGetter() {
        serviceConsent = new ServiceConsent();

        ConsentRequestEnum consent = ConsentRequestEnum.Allow;
        serviceConsent.setConsent(consent);
        assertEquals(consent, serviceConsent.getConsent());

        ServiceEnum service = ServiceEnum.BPD;
        serviceConsent.setService(service);
        assertEquals(service, serviceConsent.getService());
    }

    @Test
    void createInstance_complete() {
        serviceConsent = new ServiceConsent(ConsentRequestEnum.Allow, ServiceEnum.BPD);

        ServiceConsent consent = new ServiceConsent(ConsentRequestEnum.Allow, ServiceEnum.BPD);
        boolean equals = serviceConsent.equals(consent);
        assertTrue(equals);

        assertTrue(serviceConsent.hashCode() != 0);

        assertNotNull(serviceConsent.toString());
    }

}
