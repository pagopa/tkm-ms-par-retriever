package it.gov.pagopa.tkm.ms.parretriever.client.amex.api.security.authentication;

import it.gov.pagopa.tkm.ms.parretriever.client.amex.api.configuration.ConfigurationKeys;
import it.gov.pagopa.tkm.ms.parretriever.client.amex.api.security.Base64;

import java.net.URL;
import java.nio.charset.*;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Provides the implementation of the Amex specific HMAC algorithm.
 */
public class HmacAuthProvider extends BaseAuthProvider {

    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    private static final String SIGNATURE_FORMAT = "%s\n%s\n%s\n%s\n%s\n%s\n%s\n";
    private static final String AUTH_HEADER_FORMAT = "MAC id=\"%s\",ts=\"%s\",nonce=\"%s\",bodyhash=\"%s\",mac=\"%s\"";

    /**
     * Generates the Amex specific authentication headers required to support the HMCA authentication schema.
     * The authentication header will be calculated based on the current Amex HMAC algorithm.
     * The API key header will be populated by the configuration provider supplied.
     * The request ID header will be populated with a GUID.
     */
    public Map<String, String> generateAuthHeaders(String reqPayload, String requestUrl, String httpMethod) {
        String payload;
        URL url;
        String resourcePath;
        String host;
        String macAuth;
        int port;
        Map<String, String> headers = new Hashtable<>();
        try {
            url = new URL(requestUrl);
            resourcePath = url.getPath();
            host = url.getHost().trim().toLowerCase();
            port = (url.getPort() == -1) ? url.getDefaultPort() : url.getPort();
            payload = (reqPayload == null) ? "" : reqPayload;
            macAuth = generateMacHeader(getConfigurationValue(ConfigurationKeys.CLIENT_KEY),
                    getConfigurationValue(ConfigurationKeys.CLIENT_SECRET), resourcePath, host, port, httpMethod, payload);
            headers.put(AuthHeaderNames.AUTHORIZATION, macAuth);
            headers.put(AuthHeaderNames.X_AMEX_API_KEY, getConfigurationValue(ConfigurationKeys.CLIENT_KEY));
            headers.put(AuthHeaderNames.X_AMEX_REQUEST_ID, getRequestUUID());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Collections.unmodifiableMap(headers);
    }

    final String generateMacHeader(String client_id,
                                   String client_secret, String resourcePath, String host, int port,
                                   String httpMethod, String payload) throws Exception {
        String nonce = UUID.randomUUID().toString();
        String ts = "" + System.currentTimeMillis();

        return generateMacHeader(client_id, client_secret, resourcePath, host, port, httpMethod, payload, nonce, ts);
    }

    final String generateMacHeader(String client_id,
                                   String client_secret, String resourcePath, String host, int port,
                                   String httpMethod, String payload, String nonce, String ts) throws Exception {
        SecretKeySpec signingKey = new SecretKeySpec(
                client_secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        mac.init(signingKey);

        // create the bodyHash value by hashing the payload and encoding it
        byte[] rawBodyHash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        String bodyHash = Base64.encodeBytes(rawBodyHash);

        //The order is CRITICAL!
        //Timestamp + \n + nonce + \n+ httpmethod + \n + path + \n +host + \n + port + \n +hash + \n
        String signature = String.format(SIGNATURE_FORMAT, ts, nonce, httpMethod, resourcePath, host, port, bodyHash);

        // Generate signature using client secret (crypto initialized above)
        byte[] signatureBytes = mac.doFinal(signature.getBytes(StandardCharsets.UTF_8));

        // now encode the cypher for the web
        String signatureStr = Base64.encodeBytes(signatureBytes);
        return String.format(AUTH_HEADER_FORMAT, client_id, ts, nonce, bodyHash, signatureStr);
    }

}
