package it.gov.pagopa.tkm.ms.parretriever.client.internal.consentmanager.model.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestCardServiceConsent {

    private CardServiceConsent cardServiceConsent;

    @Test
    void verifyProperties_setterGetter() {
        cardServiceConsent = new CardServiceConsent(null, null);

        Set<ServiceConsent> serviceConsents = extracted();
        cardServiceConsent.setServiceConsents(serviceConsents);
        assertEquals(serviceConsents, cardServiceConsent.getServiceConsents());

        cardServiceConsent.setHpan("hpan");
        assertEquals("hpan", cardServiceConsent.getHpan());
    }

    private Set<ServiceConsent> extracted() {
        Set<ServiceConsent> serviceConsentSet = new HashSet<>();
        serviceConsentSet.add(new ServiceConsent());
        return serviceConsentSet;
    }

}
