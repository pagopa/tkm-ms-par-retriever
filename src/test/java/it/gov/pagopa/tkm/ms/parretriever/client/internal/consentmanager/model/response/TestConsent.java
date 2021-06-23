package it.gov.pagopa.tkm.ms.parretriever.client.internal.consentmanager.model.response;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestConsent {

    private Consent consent;

    @Test
    void verifyProperties_setterGetter() {
        consent = new Consent();

        ConsentRequestEnum consentRequestEnum = ConsentRequestEnum.Allow;
        consent.setConsent(consentRequestEnum);
        assertEquals(consentRequestEnum, consent.getConsent());

        String hpan = "b133a0c0e9bee3be20163d2ad31d6248db292aa6dcb1ee087a2aa50e0fc75ae2";
        consent.setHpan(hpan);
        assertEquals(hpan, consent.getHpan());

        Set<ServiceEnum> serviceEnumSet = getServiceEnums();
        consent.setServices(serviceEnumSet);
        assertEquals(serviceEnumSet, consent.getServices());

        assertTrue(consent.isPartial());
        consent.setHpan(null);
        assertFalse(consent.isPartial());
    }

    @NotNull
    private Set<ServiceEnum> getServiceEnums() {
        Set<ServiceEnum> serviceEnumSet = new HashSet<>();
        serviceEnumSet.add(ServiceEnum.BPD);
        serviceEnumSet.add(ServiceEnum.FA);
        return serviceEnumSet;
    }

    @Test
    void createInstance_complete() {
        consent = new Consent(ConsentRequestEnum.Allow,
                "b133a0c0e9bee3be20163d2ad31d6248db292aa6dcb1ee087a2aa50e0fc75ae2", getServiceEnums());

        Consent eqObj = new Consent.ConsentBuilder()
                .consent(ConsentRequestEnum.Allow)
                .hpan("b133a0c0e9bee3be20163d2ad31d6248db292aa6dcb1ee087a2aa50e0fc75ae2")
                .services(getServiceEnums()).build();
        boolean equals = consent.equals(eqObj);
        assertTrue(equals);

        assertTrue(consent.hashCode() != 0);

        assertEquals(consent.toString(), eqObj.toString());

        assertTrue(consent.canEqual(eqObj));

        assertNotNull(new Consent.ConsentBuilder()
                .consent(ConsentRequestEnum.Deny)
                .hpan("hpan")
                .services(getServiceEnums())
                .toString());
    }

}
