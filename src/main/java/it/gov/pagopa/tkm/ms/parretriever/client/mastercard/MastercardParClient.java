package it.gov.pagopa.tkm.ms.parretriever.client.mastercard;

import com.google.gson.*;
import com.mastercard.developer.encryption.*;
import com.mastercard.developer.interceptors.*;
import com.mastercard.developer.utils.*;
import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.*;
import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.model.*;
import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.io.*;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.time.*;
import java.time.temporal.*;

import static it.gov.pagopa.tkm.ms.parretriever.client.mastercard.constant.Constants.SIGNING_KEY_ALIAS;

//TODO: REMOVE CONTROLLER ANNOTATIONS
@RestController
@RequestMapping("/test")
public class MastercardParClient {

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${keyvault.mastercardResponsePrivateKey}")
    private String privateEncryptionKey;

    @Value("${blobstorage.mastercard.publicEncryptionKey}")
    private Resource publicEncryptionKey;

    @Value("${blobstorage.mastercard.signingKeyCert}")
    private Resource signingKeyCert;

    @Value("${keyvault.mastercardKeyStorePwd}")
    private String signingKeyPassword;

    @Value("${keyvault.mastercardApiKey}")
    private String consumerKey;

    @GetMapping
    public String callApi(@RequestParam("acc") String accountNumber, @RequestParam("id") String requestId) throws Exception {
        ApiClient client = buildApiClient();
        ParApi api = new ParApi(client);
        ParRequest parRequest = buildRequest(accountNumber, requestId);
        ParResponse parResponse = api.getParPost(parRequest);
        //String decPar = FieldLevelEncryption.decryptPayload(new Gson().toJson(parResponse), config);
        return new Gson().toJson(parResponse);
    }

    private FieldLevelEncryptionConfig buildEncryptionConfig() throws Exception {
        return FieldLevelEncryptionConfigBuilder.aFieldLevelEncryptionConfig()
                .withEncryptionPath("$.encryptedPayload.encryptedData", "$.encryptedPayload")
                .withDecryptionPath("$.encryptedPayload", "$.encryptedPayload.encryptedData")
                .withEncryptionCertificate(loadEncryptionCertificate(resourceAsString(publicEncryptionKey).getBytes()))
                .withDecryptionKey(EncryptionUtils.loadDecryptionKey(privateEncryptionKeyAsFile()))
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

    private ParRequest buildRequest(String accountNumber, String requestId) {
        String timestamp = ZonedDateTime.now().plus(1, ChronoUnit.DAYS).toString();
        ParRequestEncryptedData encryptedData = new ParRequestEncryptedData(accountNumber, timestamp);
        ParRequestEncryptedPayload encryptedPayload = new ParRequestEncryptedPayload(encryptedData);
        return new ParRequest(requestId, encryptedPayload);
    }

    private Certificate loadEncryptionCertificate(byte[] certificate) throws CertificateException {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        return factory.generateCertificate(new ByteArrayInputStream(certificate));
    }

    private String resourceAsString(Resource resource) throws IOException {
        return StreamUtils.copyToString(resource.getInputStream(), Charset.defaultCharset());
    }

    //TODO: FIND ANOTHER WAY
    private String privateEncryptionKeyAsFile() throws IOException {
        File file = new File("private_key.pem");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(privateEncryptionKey.getBytes());
        }
        return file.getAbsolutePath();
    }

}
