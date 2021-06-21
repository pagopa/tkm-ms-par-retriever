package it.gov.pagopa.tkm.ms.parretriever.client.internal.consentmanager.model.response;

import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.smartcardio.Card;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestConsentResponse {

    private ConsentResponse consentResponse;

    @Test
    void verifyProperties_setterGetter() {
        consentResponse = new ConsentResponse();

        ConsentEntityEnum consentEntityEnum = ConsentEntityEnum.Allow;
        consentResponse.setConsent(consentEntityEnum);
        assertEquals(consentEntityEnum, consentResponse.getConsent());

        Instant now = Instant.now();
        consentResponse.setLastUpdateDate(now);
        assertEquals(now, consentResponse.getLastUpdateDate());

        Set<CardServiceConsent> serviceConsentSet = getCardServiceConsents();
        consentResponse.setDetails(serviceConsentSet);
        assertEquals(serviceConsentSet, consentResponse.getDetails());
    }

    @NotNull
    private Set<CardServiceConsent> getCardServiceConsents() {
        Set<CardServiceConsent> serviceConsentSet = new HashSet<>();
        serviceConsentSet.add(new CardServiceConsent());
        return serviceConsentSet;
    }

    @Test
    void createInstance_complete() {
        Instant now = Instant.now();

        consentResponse = new ConsentResponse(ConsentEntityEnum.Allow, now, getCardServiceConsents());

        ConsentResponse response = new ConsentResponse.ConsentResponseBuilder().consent(ConsentEntityEnum.Allow)
                .lastUpdateDate(now).details(getCardServiceConsents()).build();
        boolean equals = consentResponse.equals(response);
        assertTrue(equals);

        assertTrue(consentResponse.hashCode() != 0);

        assertNotNull(consentResponse.toString());

        assertTrue(consentResponse.canEqual(response));
    }

}
