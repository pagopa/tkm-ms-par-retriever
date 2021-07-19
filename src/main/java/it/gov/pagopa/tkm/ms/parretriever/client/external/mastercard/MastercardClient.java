package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard;

import com.mastercard.developer.encryption.FieldLevelEncryptionConfig;
import com.mastercard.developer.encryption.FieldLevelEncryptionConfigBuilder;
import com.mastercard.developer.interceptors.OkHttpFieldLevelEncryptionInterceptor;
import com.mastercard.developer.interceptors.OkHttpOAuth1Interceptor;
import com.mastercard.developer.utils.AuthenticationUtils;
import com.nimbusds.jose.util.Base64;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.ParApi;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model.MastercardParRequest;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model.MastercardParResponse;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model.ParRequestEncryptedData;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model.ParRequestEncryptedPayload;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.util.ApiClient;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.util.EncryptionUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

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

    private static final String SIGNING_KEY_ALIAS = "keyalias";

    private ParApi api;

    @PostConstruct
    public void init() throws Exception {
        api = new ParApi(buildApiClient());
    }

    @CircuitBreaker(name = "mastercardClientCircuitBreaker", fallbackMethod = "getParFallback")
    public String getPar(String accountNumber) throws Exception {
   /*    System.out.println("\n [GET PAR INVOKED]");

        Timestamp now =  new Timestamp(System.currentTimeMillis());
        Timestamp switchToCorrectUrlTimestamp=null;
        Timestamp switchToFakeUrlTimestamp=null;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ITALIAN);
            Date switchToFakeUrlParsedDate = dateFormat.parse("2021-07-19 14:35:20.000");
            switchToFakeUrlTimestamp = new java.sql.Timestamp(switchToFakeUrlParsedDate.getTime());
            Date switchToCorrectUrlParsedDate = dateFormat.parse("2021-07-19 14:37:20.000");
            switchToCorrectUrlTimestamp = new java.sql.Timestamp(switchToCorrectUrlParsedDate.getTime());
        } catch(Exception e) { //this generic but you can control another types of exception
            // look the origin of excption
        }
        String fake_url = "https://sandbox.api.mistercard.com/bar/baymentaccountreference/1/0";

        String url= now.before(switchToFakeUrlTimestamp)? fake_url: retrieveParUrl;

       if (now.before(switchToFakeUrlTimestamp)){
           url = retrieveParUrl;
       } else if (now.after(switchToFakeUrlTimestamp) && now.before(switchToCorrectUrlTimestamp)){
           url = fake_url;
       } else if (now.after(switchToCorrectUrlTimestamp)){
           url = retrieveParUrl;
       }

        System.out.println("..............CURRENT URL: " + url );

      //  MastercardParResponse mastercardParResponse = api.getParPost(retrieveParUrl, buildRequest(accountNumber,
       //         UUID.randomUUID().toString())); */
        MastercardParResponse mastercardParResponse = api.getParPost(retrieveParUrl, buildRequest(accountNumber,
                UUID.randomUUID().toString()));


        if (mastercardParResponse != null && mastercardParResponse.getEncryptedPayload() != null &&
                mastercardParResponse.getEncryptedPayload().getEncryptedData() != null) {
           return  mastercardParResponse.getEncryptedPayload().getEncryptedData().getPaymentAccountReference();
        }
        return null;
    }

    public String getParFallback(String accountNumber, Throwable t ){
        log.info("MASTERCARD fallback for get par - cause {}", t.toString());
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

    private MastercardParRequest buildRequest(String accountNumber, String requestId) {
        return new MastercardParRequest(requestId,
                new ParRequestEncryptedPayload(new ParRequestEncryptedData(accountNumber, ZonedDateTime.now().plus(1,
                        ChronoUnit.DAYS).toString())));
    }

    private Certificate loadEncryptionCertificate(Base64 certificate) throws CertificateException {
        return CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(
                certificate.decode()));
    }

}
