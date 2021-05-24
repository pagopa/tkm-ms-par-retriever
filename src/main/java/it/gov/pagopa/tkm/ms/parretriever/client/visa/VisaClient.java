package it.gov.pagopa.tkm.ms.parretriever.client.visa;

import com.fasterxml.jackson.databind.*;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.util.IOUtils;
import it.gov.pagopa.tkm.ms.parretriever.client.visa.model.request.*;
import it.gov.pagopa.tkm.ms.parretriever.client.visa.model.response.*;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Sequence;
import org.springframework.beans.factory.annotation.*;
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
public class VisaClient {

    @Autowired
    private ObjectMapper mapper;

    private static final String KEYSTORE_PATH = "";
    private static final String KEYSTORE_PASSWORD = "";
    private static final String USER_ID = "";
    private static final String PASSWORD = "";
    private static final String VISA_PAR_API_PATH = "/par/v1/inquiry";
    private static final String MLE_CLIENT_PRIVATE_KEY_PATH = "";
    private static final String MLE_SERVER_PUBLIC_CERTIFICATE_PATH = "";
    private static final String KEY_ID = "";
    private static final String CLIENT_ID = "";

    public String getPar(String pan) throws Exception {
        VisaParRequest visaParRequest = new VisaParRequest(
                CLIENT_ID,
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
        HttpURLConnection con = (HttpURLConnection) new URL(VISA_PAR_API_PATH).openConnection();
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream keystoreInputStream = new FileInputStream(KEYSTORE_PATH);
        keystore.load(keystoreInputStream, KEYSTORE_PASSWORD.toCharArray());
        keystoreInputStream.close();
        KeyStore ks = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream(KEYSTORE_PATH)) {
            ks.load(fis, KEYSTORE_PASSWORD.toCharArray());
        }
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, KEYSTORE_PASSWORD.toCharArray());
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, null);
        if (con instanceof HttpsURLConnection) {
            ((HttpsURLConnection) con).setSSLSocketFactory(sslContext.getSocketFactory());
        }
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("keyId", VisaClient.KEY_ID);
        byte[] encodedAuth = Base64.getEncoder().encode((USER_ID + ":" + PASSWORD).getBytes(StandardCharsets.UTF_8));
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
        System.out.println("Http Status: " + status);
        BufferedReader in;
        if (status == 200) {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            System.out.println("Two-Way (Mutual) SSL test failed");
        }
        String response;
        StringBuilder content = new StringBuilder();
        while ((response = in.readLine()) != null) {
            content.append(response);
        }
        in.close();
        con.disconnect();
        con.getHeaderFields().forEach((k, v) -> System.out.println(k + " : " + v));
        String responseAsString = content.toString();
        return mapper.readValue(responseAsString, VisaParEncryptedResponse.class);
    }

    private String getEncryptedPayload(String requestPayload) throws CertificateException, JOSEException, IOException {
        JWEHeader.Builder headerBuilder = new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A128GCM);
        headerBuilder.keyID(VisaClient.KEY_ID);
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

    private RSAPublicKey getRSAPublicKey() throws CertificateException, IOException {
        final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
        final String END_CERT = "-----END CERTIFICATE-----";
        final String pemEncodedPublicKey = IOUtils.readFileToString(new File(VisaClient.MLE_SERVER_PUBLIC_CERTIFICATE_PATH), StandardCharsets.UTF_8);
        final com.nimbusds.jose.util.Base64 base64 = new com.nimbusds.jose.util.Base64(pemEncodedPublicKey.replaceAll(BEGIN_CERT, "").replaceAll(END_CERT, ""));
        final Certificate cf = CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(base64.decode()));
        return (RSAPublicKey) cf.getPublicKey();
    }

    private PrivateKey getRSAPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        final String BEGIN_RSA_PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----";
        final String END_RSA_PRIVATE_KEY = "-----END RSA PRIVATE KEY-----";

        final String pemEncodedKey = IOUtils.readFileToString(new File(VisaClient.MLE_CLIENT_PRIVATE_KEY_PATH), StandardCharsets.UTF_8);
        final com.nimbusds.jose.util.Base64 base64 = new com.nimbusds.jose.util.Base64(pemEncodedKey.replaceAll(BEGIN_RSA_PRIVATE_KEY, "").replaceAll(END_RSA_PRIVATE_KEY, ""));
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
