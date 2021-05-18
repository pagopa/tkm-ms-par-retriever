package it.gov.pagopa.tkm.ms.parretriever.visa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.util.IOUtils;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Sequence;

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
import java.util.Base64;
import java.util.Enumeration;

public class VisaApiClient {

    public static final String VISA_BASE_URL = "";
    private static final String KEYSTORE_PATH = "";
    private static final String KEYSTORE_PASSWORD = "";
    private static final String USER_ID = "";
    private static final String PASSWORD = "";

/*    public static void main(String[] args) throws Exception {
        String reqPayload = "{\n" +
                "\"clientId\": \"0123456789012345678901234567999\",\n" +
                "\"correlatnId\": \"0123456789012345678901234567000\",\n" +
                "\"primaryAccount\": \"1234567898000000\",\n" +
                "}";
        String encryptedPayload = getEncryptedPayload(MLE_SERVER_PUBLIC_CERTIFICATE_PATH, reqPayload, KEY_ID);
        String encryptedResponseStr = invokeAPI("/par/v1/inquiry", "POST", encryptedPayload);
        EncryptedResponse encryptedResponse = new ObjectMapper().readValue(encryptedResponseStr, EncryptedResponse.class);
        System.out.println("Encrypted Response: \n" + encryptedResponse.getEncData());
        String decryptedResponse = getDecryptedPayload(MLE_CLIENT_PRIVATE_KEY_PATH, encryptedResponse);
        System.out.println("Decrypted Response: \n" + decryptedResponse);
    }*/

    public static String invokeAPI(final String resourcePath, String httpMethod, String payload, String keyId) throws Exception {
        String url = VISA_BASE_URL + resourcePath;
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream keystoreInputStream = new FileInputStream(KEYSTORE_PATH);
        keystore.load(keystoreInputStream, KEYSTORE_PASSWORD.toCharArray());
        keystoreInputStream.close();
        // Make a KeyStore from the PKCS-12 file
        KeyStore ks = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream(KEYSTORE_PATH)) {
            ks.load(fis, KEYSTORE_PASSWORD.toCharArray());
        }

        // Make a KeyManagerFactory from the KeyStore
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, KEYSTORE_PASSWORD.toCharArray());

        // Now make an SSL Context with our Key Manager and the default Trust Manager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, null);
        if (con instanceof HttpsURLConnection) {
            ((HttpsURLConnection) con).setSSLSocketFactory(sslContext.getSocketFactory());
        }

        con.setRequestMethod(httpMethod);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("keyId", keyId);

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
        return content.toString();
    }

    public static String getEncryptedPayload(String mleServerPublicCertificatePath, String requestPayload, String keyId) throws CertificateException, JOSEException, IOException {
        JWEHeader.Builder headerBuilder = new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A128GCM);
        headerBuilder.keyID(keyId);
        headerBuilder.customParam("iat", System.currentTimeMillis());
        JWEObject jweObject = new JWEObject(headerBuilder.build(), new Payload(requestPayload));
        jweObject.encrypt(new RSAEncrypter(getRSAPublicKey(mleServerPublicCertificatePath)));
        return "{\"encData\":\"" + jweObject.serialize() + "\"}";
    }

    public static String getDecryptedPayload(String mleClientPrivateKeyPath, EncryptedResponse encryptedPayload) throws ParseException, NoSuchAlgorithmException, IOException, InvalidKeySpecException, JOSEException {
        String response = encryptedPayload.getEncData();
        JWEObject jweObject = JWEObject.parse(response);
        PrivateKey privateKey = getRSAPrivateKey(mleClientPrivateKeyPath);
        jweObject.decrypt(new RSADecrypter(privateKey));
        response = jweObject.getPayload().toString();
        return response;
    }

    private static RSAPublicKey getRSAPublicKey(final String mleServerPublicCertificatePath) throws CertificateException, IOException {
        final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
        final String END_CERT = "-----END CERTIFICATE-----";
        final String pemEncodedPublicKey = IOUtils.readFileToString(new File(mleServerPublicCertificatePath), StandardCharsets.UTF_8);
        final com.nimbusds.jose.util.Base64 base64 = new com.nimbusds.jose.util.Base64(pemEncodedPublicKey.replaceAll(BEGIN_CERT, "").replaceAll(END_CERT, ""));
        final Certificate cf = CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(base64.decode()));
        return (RSAPublicKey) cf.getPublicKey();
    }

    private static PrivateKey getRSAPrivateKey(String mleClientPrivateKeyPath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        final String BEGIN_RSA_PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----";
        final String END_RSA_PRIVATE_KEY = "-----END RSA PRIVATE KEY-----";

        final String pemEncodedKey = IOUtils.readFileToString(new File(mleClientPrivateKeyPath), StandardCharsets.UTF_8);
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

    public static class EncryptedResponse {

        String encData;

        public String getEncData() {
            return encData;
        }

    }

}
