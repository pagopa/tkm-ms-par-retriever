package it.gov.pagopa.tkm.ms.parretriever.service.impl;

import com.azure.identity.*;
import com.azure.security.keyvault.keys.*;
import com.azure.security.keyvault.keys.cryptography.*;
import com.azure.security.keyvault.keys.cryptography.models.*;
import it.gov.pagopa.tkm.ms.parretriever.service.*;
import lombok.extern.log4j.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;

import javax.annotation.*;

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
        cryptographyClient = new CryptographyClientBuilder()
                .keyIdentifier(new KeyClientBuilder().vaultUrl(uri).credential(clientSecretCredential).buildClient()
                        .getKey(cryptographicKeyId).getId())
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
        DecryptResult decryptResult = cryptographyClient.decrypt(EncryptionAlgorithm.RSA_OAEP_256,
                Base64Utils.decodeFromString(toDecrypt));
        if (decryptResult == null || ArrayUtils.isEmpty(decryptResult.getPlainText())) {
            log.trace("\tCan't cryptography the string");
            return null;
        }
        return new String(decryptResult.getPlainText());
    }

}
