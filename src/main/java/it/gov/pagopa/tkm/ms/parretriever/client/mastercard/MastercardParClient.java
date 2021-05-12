package it.gov.pagopa.tkm.ms.parretriever.client.mastercard;

import com.google.gson.*;
import com.mastercard.developer.encryption.*;
import com.mastercard.developer.interceptors.*;
import com.mastercard.developer.utils.*;
import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.*;
import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.model.*;
import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.util.*;
import okhttp3.logging.*;

import java.time.*;
import java.time.temporal.*;

import static it.gov.pagopa.tkm.ms.parretriever.client.mastercard.constant.Constants.*;

public class MastercardParClient {

    public static void main(String[] args) throws Exception {
        callApi("5123456789012342", "234567");
    }

    public static void callApi(String accountNumber, String requestId) throws Exception {
        ApiClient client = buildApiClient();
        ParApi api = new ParApi(client);
        ParRequest parRequest = buildRequest(accountNumber, requestId);
        ParResponse parResponse = api.getParPost(parRequest);
        //String decPar = FieldLevelEncryption.decryptPayload(new Gson().toJson(parResponse), config);
        System.out.println(new Gson().toJson(parResponse));
    }

    private static FieldLevelEncryptionConfig buildEncryptionConfig() throws Exception {
        return FieldLevelEncryptionConfigBuilder.aFieldLevelEncryptionConfig()
                .withEncryptionPath("$.encryptedPayload.encryptedData", "$.encryptedPayload")
                .withDecryptionPath("$.encryptedPayload", "$.encryptedPayload.encryptedData")
                .withEncryptionCertificate(EncryptionUtils.loadEncryptionCertificate(CERT_PATH))
                .withDecryptionKey(EncryptionUtils.loadDecryptionKey(PRIVATE_KEY_PATH))
                .withOaepPaddingDigestAlgorithm("SHA-512")
                .withEncryptedValueFieldName("encryptedData")
                .withEncryptedKeyFieldName("encryptedKey")
                .withIvFieldName("iv")
                .withOaepPaddingDigestAlgorithmFieldName("oaepHashingAlgorithm")
                .withEncryptionCertificateFingerprintFieldName("publicKeyFingerprint")
                .withFieldValueEncoding(FieldLevelEncryptionConfig.FieldValueEncoding.HEX)
                .build();
    }

    private static ApiClient buildApiClient() throws Exception {
        return new ApiClient(
                new OkHttpFieldLevelEncryptionInterceptor(buildEncryptionConfig()),
                new OkHttpOAuth1Interceptor(CONSUMER_KEY, AuthenticationUtils.loadSigningKey(PKCS12_PATH, KEY_ALIAS, KEYSTORE_PASSWORD)),
                //TODO: Remove logging-interceptor from dependencies when done with development
                new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        );
    }

    private static ParRequest buildRequest(String accountNumber, String requestId) {
        String timestamp = ZonedDateTime.now().plus(1, ChronoUnit.DAYS).toString();
        ParRequestEncryptedData encryptedData = new ParRequestEncryptedData(accountNumber, timestamp);
        ParRequestEncryptedPayload encryptedPayload = new ParRequestEncryptedPayload(encryptedData);
        return new ParRequest(requestId, encryptedPayload);
    }

}
