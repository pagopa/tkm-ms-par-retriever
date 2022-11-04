package it.gov.pagopa.tkm.ms.parretriever.client.external.visa;

import com.fasterxml.jackson.databind.*;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import io.github.resilience4j.circuitbreaker.annotation.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.visa.model.request.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.visa.model.response.*;
import lombok.extern.log4j.*;
import org.bouncycastle.asn1.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.io.*;
import org.springframework.stereotype.*;

import javax.net.ssl.*;
import java.io.*;
import java.math.*;
import java.net.*;
import java.nio.charset.*;
import java.security.*;
import java.security.cert.*;
import java.security.interfaces.*;
import java.security.spec.*;
import java.text.*;
import java.time.*;
import java.util.*;

@Component
@Log4j2
public class VisaClient {

    @Autowired
    private ObjectMapper mapper;

    @Value("${blob-storage.visaPublicCert}")
    private Resource publicCert;

    @Value("${keyvault.visaKeyStorePassword}")
    private String keystorePassword;

    @Value("${keyvault.visaUserId}")
    private String userId;

    @Value("${keyvault.visaPassword}")
    private String password;

    @Value("${keyvault.visaPrivateKey}")
    private String clientPrivateKey;

    @Value("${keyvault.visaServerCertificate}")
    private String serverPublicCertificate;

    @Value("${keyvault.visaKeyId}")
    private String keyId;

    @Value("${keyvault.visaClientId}")
    private String clientId;

    @Value("${circuit-urls.visa}")
    private String retrieveParUrl;

    @CircuitBreaker(name = "visaClientCircuitBreaker", fallbackMethod = "getParFallback")
    public String getPar(String pan) throws Exception {
        VisaParDecryptedResponse decryptedResponse = getDecryptedPayload(invokeAPI(getEncryptedPayload(
                mapper.writeValueAsString(new VisaParRequest(clientId, UUID.randomUUID().toString(), pan)))));
        if (decryptedResponse != null) {
            return decryptedResponse.getPaymentAccountReference();
        }
        return null;
    }

    public String getParFallback(Throwable t ){
        log.error("VISA fallback", t);
        return null;
    }


    private VisaParEncryptedResponse invokeAPI(String payload) throws Exception {
        HttpURLConnection con = (HttpURLConnection) new URL(retrieveParUrl).openConnection();
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(publicCert.getInputStream(), keystorePassword.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, keystorePassword.toCharArray());
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, null);
        if (con instanceof HttpsURLConnection) {
            ((HttpsURLConnection) con).setSSLSocketFactory(sslContext.getSocketFactory());
        }
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("keyId", keyId);
        con.setRequestProperty("Authorization",
                "Basic " + new String(Base64.getEncoder().encode((userId + ":" + password).getBytes(
                        StandardCharsets.UTF_8))));
        if (payload != null && payload.trim().length() > 0) {
            con.setDoOutput(true);
            con.setDoInput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }
        BufferedReader in;
        if (con.getResponseCode() == 200) {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            log.error("Two-Way (Mutual) SSL test failed");
        }
        String response;
        StringBuilder content = new StringBuilder();
        while ((response = in.readLine()) != null) {
            content.append(response);
        }
        in.close();
        con.disconnect();
        return mapper.readValue(content.toString(), VisaParEncryptedResponse.class);
    }

    private String getEncryptedPayload(String requestPayload) throws CertificateException, JOSEException {
        JWEHeader.Builder headerBuilder = new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A128GCM);
        headerBuilder.keyID(keyId);
        headerBuilder.customParam("iat", Instant.now().toEpochMilli());
        JWEObject jweObject = new JWEObject(headerBuilder.build(), new Payload(requestPayload));
        jweObject.encrypt(new RSAEncrypter(getRSAPublicKey()));
        return "{\"encData\":\"" + jweObject.serialize() + "\"}";
    }

    private VisaParDecryptedResponse getDecryptedPayload(VisaParEncryptedResponse encryptedPayload)
            throws ParseException, NoSuchAlgorithmException, IOException, InvalidKeySpecException, JOSEException {
        String response = encryptedPayload.getEncData();
        JWEObject jweObject = JWEObject.parse(response);
        jweObject.decrypt(new RSADecrypter(getRSAPrivateKey()));
        response = jweObject.getPayload().toString();
        return mapper.readValue(response, VisaParDecryptedResponse.class);
    }

    private RSAPublicKey getRSAPublicKey() throws CertificateException {
        return (RSAPublicKey) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(
                new com.nimbusds.jose.util.Base64(serverPublicCertificate.replaceAll("-----BEGIN CERTIFICATE-----",
                        "").replaceAll("-----END CERTIFICATE-----", "")).decode())).getPublicKey();
    }

    private PrivateKey getRSAPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        final Enumeration<?> e = ((ASN1Sequence) ASN1Sequence.fromByteArray(new com.nimbusds.jose.util.Base64(clientPrivateKey.replaceAll("-----BEGIN RSA PRIVATE KEY-----", "").replaceAll("-----END RSA PRIVATE KEY-----", "")).decode())).getObjects();
        final BigInteger v = ((ASN1Integer) e.nextElement()).getValue();
        int version = v.intValue();
        if (version != 0 && version != 1) {
            throw new IllegalArgumentException("wrong version for RSA private key");
        }
        final BigInteger modulus = ((ASN1Integer) e.nextElement()).getValue();
        ((ASN1Integer) e.nextElement()).getValue();
        BigInteger privateExponent = ((ASN1Integer) e.nextElement()).getValue();
        ((ASN1Integer) e.nextElement()).getValue();
        ((ASN1Integer) e.nextElement()).getValue();
        ((ASN1Integer) e.nextElement()).getValue();
        ((ASN1Integer) e.nextElement()).getValue();
        ((ASN1Integer) e.nextElement()).getValue();
        RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(modulus, privateExponent);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(privateKeySpec);
    }

}
