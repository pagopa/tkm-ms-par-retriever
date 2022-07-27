package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard;

import com.mastercard.developer.encryption.*;
import com.mastercard.developer.interceptors.*;
import com.mastercard.developer.utils.*;
import com.nimbusds.jose.util.Base64;
import io.github.resilience4j.circuitbreaker.annotation.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.util.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.util.EncryptionUtils;
import lombok.extern.log4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.*;

import javax.annotation.*;
import java.io.*;
import java.security.cert.*;
import java.time.*;
import java.time.temporal.*;
import java.util.*;

@Log4j2
@Service
public class MastercardClient {

    @Value("${keyvault.mastercardResponsePrivateKey}")
    private String privateDecryptionKey;

    @Value("${keyvault.mastercardPublicEncryptionKey}")
    private String publicEncryptionKey;

    @Value("${blob-storage.mastercardSigningKeyCert}")
    private Resource signingKeyCert;

    @Value("${keyvault.mastercardKeyStorePwd}")
    private String signingKeyPassword;

    @Value("${keyvault.mastercardApiKey}")
    private String consumerKey;

    @Value("${circuit-urls.mastercard}")
    private String retrieveParUrl;

    @Value("${circuit-activation.mastercard}")
    private String isMastercardActive;

    private static final String SIGNING_KEY_ALIAS = "keyalias";

    private ParApi api;

    @PostConstruct
    public void init() throws Exception {
        if (isMastercardActive.equals("true")) {
            api = new ParApi(buildApiClient());
        }
    }

    @CircuitBreaker(name = "mastercardClientCircuitBreaker", fallbackMethod = "getParFallback")
    public String getPar(String accountNumber) throws Exception {
        MastercardParResponse mastercardParResponse = api.getParPost(retrieveParUrl, buildRequest(accountNumber,
                UUID.randomUUID().toString()));

        if (mastercardParResponse != null && mastercardParResponse.getEncryptedPayload() != null &&
                mastercardParResponse.getEncryptedPayload().getEncryptedData() != null) {
           return  mastercardParResponse.getEncryptedPayload().getEncryptedData().getPaymentAccountReference();
        }
        return null;
    }

    public String getParFallback(String accountNumber, Throwable t ){
        log.info(String.format("MASTERCARD fallback for get par of account number %s- cause {}", accountNumber), t.toString());
        return "MASTERCARD fallback for get par. Some error occurred while calling get Par for Mastercard client";
    }

    private FieldLevelEncryptionConfig buildEncryptionConfig() throws Exception {
        return FieldLevelEncryptionConfigBuilder.aFieldLevelEncryptionConfig()
                .withEncryptionPath("$.encryptedPayload.encryptedData", "$.encryptedPayload")
                .withDecryptionPath("$.encryptedPayload", "$.encryptedPayload.encryptedData")
                .withEncryptionCertificate(loadEncryptionCertificate(new Base64(publicEncryptionKey.replaceAll(
                        "-----BEGIN CERTIFICATE-----", "").replaceAll("-----END CERTIFICATE-----", ""))))
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
                new OkHttpOAuth1Interceptor(consumerKey,
                        AuthenticationUtils.loadSigningKey(signingKeyCert.getInputStream(), SIGNING_KEY_ALIAS,
                                signingKeyPassword))
        );
    }

    MastercardParRequest buildRequest(String accountNumber, String requestId) {
        return new MastercardParRequest(requestId,
                new ParRequestEncryptedPayload(new ParRequestEncryptedData(accountNumber, ZonedDateTime.now().plus(1,
                        ChronoUnit.DAYS).toString())));
    }

    private Certificate loadEncryptionCertificate(Base64 certificate) throws CertificateException {
        return CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(
                certificate.decode()));
    }

}
