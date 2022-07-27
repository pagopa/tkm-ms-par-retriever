package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.util;

import it.gov.pagopa.tkm.ms.parretriever.constant.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.junit.jupiter.*;

import java.security.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class TestEncryptionUtils {

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
