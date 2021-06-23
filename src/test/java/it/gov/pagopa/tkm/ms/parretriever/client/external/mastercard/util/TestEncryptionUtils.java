package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.util;

import it.gov.pagopa.tkm.ms.parretriever.constant.DefaultBeans;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestEncryptionUtils {

    private DefaultBeans testBeans;

    @BeforeEach
    void init() {
        testBeans = new DefaultBeans();
    }

    @Test
    void loadDecriptionKey_notNull() throws GeneralSecurityException {
        PrivateKey privateKey = EncryptionUtils.loadDecryptionKey(testBeans.MASTERCARD_RESPONSE_PRIVATE_KEY);
        Assertions.assertNotNull(privateKey);
    }

    @Test
    void newEncryptionUtils_constructor() {
        EncryptionUtils encryptionUtils = new EncryptionUtils();
        Assertions.assertNotNull(encryptionUtils);
    }

    @Test
    void invalidKeyFormat_throwsException() {
        String privateKey = "password";
        Assertions.assertThrows(IllegalArgumentException.class, () -> EncryptionUtils.loadDecryptionKey(privateKey));
    }

}
