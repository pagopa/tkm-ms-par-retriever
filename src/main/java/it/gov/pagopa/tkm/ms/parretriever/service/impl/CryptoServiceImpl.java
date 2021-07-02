package it.gov.pagopa.tkm.ms.parretriever.service.impl;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.security.keyvault.keys.KeyClient;
import com.azure.security.keyvault.keys.KeyClientBuilder;
import com.azure.security.keyvault.keys.cryptography.CryptographyClient;
import com.azure.security.keyvault.keys.cryptography.CryptographyClientBuilder;
import com.azure.security.keyvault.keys.cryptography.models.DecryptResult;
import com.azure.security.keyvault.keys.cryptography.models.EncryptionAlgorithm;
import com.azure.security.keyvault.keys.models.KeyVaultKey;
import it.gov.pagopa.tkm.ms.parretriever.service.CryptoService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;

@Service
@Log4j2
public class CryptoServiceImpl implements CryptoService {

    @Value("${azure.keyvault.client-id}")
    private String clientId;

    @Value("${azure.keyvault.client-key}")
    private String clientKey;

    @Value("${azure.keyvault.tenant-id}")
    private String tenantId;

    @Value("${azure.keyvault.uri}")
    private String uri;

    @Value("${keyvault.cryptographicKeyId}")
    private String cryptographicKeyId;

    private CryptographyClient cryptographyClient;

    @PostConstruct
    public void init() {
        ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
                .clientId(clientId)
                .clientSecret(clientKey)
                .tenantId(tenantId)
                .build();
        KeyClient keyClient = new KeyClientBuilder().vaultUrl(uri).credential(clientSecretCredential).buildClient();
        KeyVaultKey keyVaultKey = keyClient.getKey(cryptographicKeyId);
        cryptographyClient = new CryptographyClientBuilder()
                .keyIdentifier(keyVaultKey.getId())
                .credential(clientSecretCredential)
                .buildClient();
    }

    @Override
    public String decrypt(String toDecrypt) {
        log.trace(toDecrypt + ":");
        if (StringUtils.isBlank(toDecrypt)) {
            log.trace("\tEmpty string");
            return null;
        }
        DecryptResult decryptResult = cryptographyClient.decrypt(EncryptionAlgorithm.RSA_OAEP_256, Base64Utils.decodeFromString(toDecrypt));
        if (decryptResult == null || ArrayUtils.isEmpty(decryptResult.getPlainText())) {
            log.trace("\tCan't cryptography the string");
            return null;
        }
        return new String(decryptResult.getPlainText());
    }

}
