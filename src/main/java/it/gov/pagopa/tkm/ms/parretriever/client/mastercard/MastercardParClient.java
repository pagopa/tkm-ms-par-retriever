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
        GetPaymentAccountReferenceApi api = new GetPaymentAccountReferenceApi(client);
        ParRequest req = buildRequest(accountNumber, requestId);
        ParResponse res = api.getParPost(req);
        //String decPar = FieldLevelEncryption.decryptPayload(new Gson().toJson(res), config);
        System.out.println(new Gson().toJson(res));
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
                new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        );
    }

    private static ParRequest buildRequest(String accountNumber, String requestId) {
        String timestamp = ZonedDateTime.now().plus(1, ChronoUnit.DAYS).toString();
        ParRequestEncryptedData encData = new ParRequestEncryptedData(accountNumber, timestamp);
        ParRequestEncryptedPayload encPayload = new ParRequestEncryptedPayload(encData);
        return new ParRequest(requestId, encPayload);
    }

}
