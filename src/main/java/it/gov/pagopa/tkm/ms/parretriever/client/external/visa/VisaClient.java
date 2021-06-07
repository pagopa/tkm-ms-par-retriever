package it.gov.pagopa.tkm.ms.parretriever.client.external.visa;

import com.fasterxml.jackson.databind.*;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import it.gov.pagopa.tkm.ms.parretriever.client.external.visa.model.request.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.visa.model.response.*;
import lombok.extern.log4j.*;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Sequence;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.io.*;
import org.springframework.stereotype.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.text.ParseException;
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
    private String mleClientPrivateKey;

    @Value("${keyvault.visaServerCertificate}")
    private String mleServerPublicCertificatePath;

    @Value("${keyvault.visaKeyId}")
    private String keyId;

    @Value("${keyvault.visaClientId}")
    private String clientId;

    @Value("${circuit-urls.visa}")
    private String retrieveParUrl;

    public String getPar(String pan) throws Exception {
        VisaParRequest visaParRequest = new VisaParRequest(
                clientId,
                UUID.randomUUID().toString(),
                pan);
        String reqPayload = mapper.writeValueAsString(visaParRequest);
        String encryptedPayload = getEncryptedPayload(reqPayload);
        VisaParEncryptedResponse encryptedResponse = invokeAPI(encryptedPayload);
        VisaParDecryptedResponse decryptedResponse = getDecryptedPayload(encryptedResponse);
        if (decryptedResponse != null) {
            return decryptedResponse.getPaymentAccountReference();
        }
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
        byte[] encodedAuth = Base64.getEncoder().encode((userId + ":" + password).getBytes(StandardCharsets.UTF_8));
        String authHeaderValue = "Basic " + new String(encodedAuth);
        con.setRequestProperty("Authorization", authHeaderValue);
        if (payload != null && payload.trim().length() > 0) {
            con.setDoOutput(true);
            con.setDoInput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }
        int status = con.getResponseCode();
        BufferedReader in;
        if (status == 200) {
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
        String responseAsString = content.toString();
        return mapper.readValue(responseAsString, VisaParEncryptedResponse.class);
    }

    private String getEncryptedPayload(String requestPayload) throws CertificateException, JOSEException {
        JWEHeader.Builder headerBuilder = new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A128GCM);
        headerBuilder.keyID(keyId);
        headerBuilder.customParam("iat", System.currentTimeMillis());
        JWEObject jweObject = new JWEObject(headerBuilder.build(), new Payload(requestPayload));
        jweObject.encrypt(new RSAEncrypter(getRSAPublicKey()));
        return "{\"encData\":\"" + jweObject.serialize() + "\"}";
    }

    private VisaParDecryptedResponse getDecryptedPayload(VisaParEncryptedResponse encryptedPayload) throws ParseException, NoSuchAlgorithmException, IOException, InvalidKeySpecException, JOSEException {
        String response = encryptedPayload.getEncData();
        JWEObject jweObject = JWEObject.parse(response);
        PrivateKey privateKey = getRSAPrivateKey();
        jweObject.decrypt(new RSADecrypter(privateKey));
        response = jweObject.getPayload().toString();
        return mapper.readValue(response, VisaParDecryptedResponse.class);
    }

    private RSAPublicKey getRSAPublicKey() throws CertificateException {
        final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
        final String END_CERT = "-----END CERTIFICATE-----";
        final com.nimbusds.jose.util.Base64 base64 = new com.nimbusds.jose.util.Base64(mleServerPublicCertificatePath.replaceAll(BEGIN_CERT, "").replaceAll(END_CERT, ""));
        final Certificate cf = CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(base64.decode()));
        return (RSAPublicKey) cf.getPublicKey();
    }

    private PrivateKey getRSAPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        final String BEGIN_RSA_PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----";
        final String END_RSA_PRIVATE_KEY = "-----END RSA PRIVATE KEY-----";
        final com.nimbusds.jose.util.Base64 base64 = new com.nimbusds.jose.util.Base64(mleClientPrivateKey.replaceAll(BEGIN_RSA_PRIVATE_KEY, "").replaceAll(END_RSA_PRIVATE_KEY, ""));
        final ASN1Sequence primitive = (ASN1Sequence) ASN1Sequence.fromByteArray(base64.decode());
        final Enumeration<?> e = primitive.getObjects();
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
