package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard;

import com.mastercard.developer.encryption.*;
import com.mastercard.developer.interceptors.*;
import com.mastercard.developer.utils.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.util.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.util.EncryptionUtils;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.*;
import org.springframework.util.*;

import javax.annotation.*;
import java.io.*;
import java.nio.charset.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.time.*;
import java.time.temporal.*;
import java.util.*;

@Service
public class MastercardClient {

    @Value("${keyvault.mastercardResponsePrivateKey}")
    private String privateDecryptionKey;

    @Value("${blob-storage.mastercardPublicEncryptionKey}")
    private Resource publicEncryptionKey;

    @Value("${blob-storage.mastercardSigningKeyCert}")
    private Resource signingKeyCert;

    @Value("${keyvault.mastercardKeyStorePwd}")
    private String signingKeyPassword;

    @Value("${keyvault.mastercardApiKey}")
    private String consumerKey;

    @Value("${circuit-urls.mastercard}")
    private String retrieveParUrl;

    private static final String SIGNING_KEY_ALIAS = "keyalias";

    private ParApi api;

    @PostConstruct
    public void init() throws Exception {
        api = new ParApi(buildApiClient());
    }

    public String getPar(String accountNumber) throws Exception {
        MastercardParRequest mastercardParRequest = buildRequest(accountNumber, UUID.randomUUID().toString());
        MastercardParResponse mastercardParResponse = api.getParPost(retrieveParUrl, mastercardParRequest);
        if (mastercardParResponse != null && mastercardParResponse.getEncryptedPayload() != null && mastercardParResponse.getEncryptedPayload().getEncryptedData() != null) {
            return mastercardParResponse.getEncryptedPayload().getEncryptedData().getPaymentAccountReference();
        }
        return null;
    }

    private FieldLevelEncryptionConfig buildEncryptionConfig() throws Exception {
        return FieldLevelEncryptionConfigBuilder.aFieldLevelEncryptionConfig()
                .withEncryptionPath("$.encryptedPayload.encryptedData", "$.encryptedPayload")
                .withDecryptionPath("$.encryptedPayload", "$.encryptedPayload.encryptedData")
                .withEncryptionCertificate(loadEncryptionCertificate(resourceAsString(publicEncryptionKey).getBytes()))
                .withDecryptionKey(EncryptionUtils.loadDecryptionKey(privateDecryptionKey))
                .withOaepPaddingDigestAlgorithm("SHA-512")
                .withEncryptedValueFieldName("encryptedData")
                .withEncryptedKeyFieldName("encryptedKey")
                .withIvFieldName("iv")
                .withOaepPaddingDigestAlgorithmFieldName("oaepHashingAlgorithm")
                .withEncryptionCertificateFingerprintFieldName("publicKeyFingerprint")
                .withFieldValueEncoding(FieldLevelEncryptionConfig.FieldValueEncoding.HEX)
                .build();
    }

    private ApiClient buildApiClient() throws Exception {
        return new ApiClient(
                new OkHttpFieldLevelEncryptionInterceptor(buildEncryptionConfig()),
                new OkHttpOAuth1Interceptor(consumerKey, AuthenticationUtils.loadSigningKey(signingKeyCert.getInputStream(), SIGNING_KEY_ALIAS, signingKeyPassword))
        );
    }

    private MastercardParRequest buildRequest(String accountNumber, String requestId) {
        String timestamp = ZonedDateTime.now().plus(1, ChronoUnit.DAYS).toString();
        ParRequestEncryptedData encryptedData = new ParRequestEncryptedData(accountNumber, timestamp);
        ParRequestEncryptedPayload encryptedPayload = new ParRequestEncryptedPayload(encryptedData);
        return new MastercardParRequest(requestId, encryptedPayload);
    }

    private Certificate loadEncryptionCertificate(byte[] certificate) throws CertificateException {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        return factory.generateCertificate(new ByteArrayInputStream(certificate));
    }

    private String resourceAsString(Resource resource) throws IOException {
        return StreamUtils.copyToString(resource.getInputStream(), Charset.defaultCharset());
    }

}
