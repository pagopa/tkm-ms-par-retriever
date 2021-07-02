package it.gov.pagopa.tkm.ms.parretriever.service;

import com.azure.security.keyvault.keys.cryptography.CryptographyClient;
import com.azure.security.keyvault.keys.cryptography.models.DecryptResult;
import com.azure.security.keyvault.keys.cryptography.models.EncryptionAlgorithm;
import it.gov.pagopa.tkm.ms.parretriever.service.impl.CryptoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.Base64Utils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestCryptoService {

    @InjectMocks
    private CryptoServiceImpl cryptoService;

    @Mock
    private CryptographyClient client;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(cryptoService, "uri", "https://127.0.0.1");
        ReflectionTestUtils.setField(cryptoService, "tenantId", "TENANT_ID");
        ReflectionTestUtils.setField(cryptoService, "clientId", "CLIENT_ID");
        ReflectionTestUtils.setField(cryptoService, "clientKey", "CLIENT_KEY");
        ReflectionTestUtils.setField(cryptoService, "cryptographicKeyId", "KEY_ID");
    }

    @Test
    void createInstance_init() {
        assertThrows(RuntimeException.class, () -> cryptoService.init());
    }

    @Test
    void givenEncryptedString_returnPlaintext() {
        DecryptResult dec = new DecryptResult("PLAINTEXT".getBytes(), EncryptionAlgorithm.RSA_OAEP_256, "KEY_ID");
        when(client.decrypt(EncryptionAlgorithm.RSA_OAEP_256, Base64Utils.decodeFromString("RU5DUllQVEVE"))).thenReturn(dec);
        assertEquals("PLAINTEXT", cryptoService.decrypt("RU5DUllQVEVE"));
    }

    @Test
    void givenEmptyEncryptedString_throwException() {
        assertNull(cryptoService.decrypt(""));
    }

    @Test
    void givenEmptyDecryptResponse_throwException() {
        DecryptResult dec = new DecryptResult("".getBytes(), EncryptionAlgorithm.RSA_OAEP_256, "KEY_ID");
        when(client.decrypt(EncryptionAlgorithm.RSA_OAEP_256, Base64Utils.decodeFromString("RU5DUllQVEVE"))).thenReturn(dec);
        assertNull(cryptoService.decrypt("RU5DUllQVEVE"));
    }

}
