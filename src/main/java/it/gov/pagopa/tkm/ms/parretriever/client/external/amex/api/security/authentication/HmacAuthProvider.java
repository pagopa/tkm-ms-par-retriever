package it.gov.pagopa.tkm.ms.parretriever.client.external.amex.api.security.authentication;

import it.gov.pagopa.tkm.ms.parretriever.client.external.amex.api.configuration.ConfigurationKeys;
import it.gov.pagopa.tkm.ms.parretriever.client.external.amex.api.security.Base64;

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
        Map<String, String> headers = new Hashtable<>();
        try {
            URL url = new URL(requestUrl);
            headers.put(AuthHeaderNames.AUTHORIZATION,
                    generateMacHeader(getConfigurationValue(ConfigurationKeys.CLIENT_KEY),
                            getConfigurationValue(ConfigurationKeys.CLIENT_SECRET), url.getPath(),
                            url.getHost().trim().toLowerCase(),
                            (url.getPort() == -1) ? url.getDefaultPort() : url.getPort(), httpMethod,
                            (reqPayload == null) ? "" : reqPayload));
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
        return generateMacHeader(client_id, client_secret, resourcePath, host, port, httpMethod, payload,
                UUID.randomUUID().toString(), "" + System.currentTimeMillis());
    }

    final String generateMacHeader(String client_id,
                                   String client_secret, String resourcePath, String host, int port,
                                   String httpMethod, String payload, String nonce, String ts) throws Exception {
        Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        mac.init(new SecretKeySpec(client_secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256_ALGORITHM));

        // create the bodyHash value by hashing the payload and encoding it
        String bodyHash = Base64.encodeBytes(mac.doFinal(payload.getBytes(StandardCharsets.UTF_8)));

        //The order is CRITICAL!
        //Timestamp + \n + nonce + \n+ httpmethod + \n + path + \n +host + \n + port + \n +hash + \n
        // Generate signature using client secret (crypto initialized above)
        // now encode the cypher for the web
        return String.format(AUTH_HEADER_FORMAT, client_id, ts, nonce, bodyHash,
                Base64.encodeBytes(mac.doFinal(String.format(SIGNATURE_FORMAT, ts, nonce, httpMethod, resourcePath,
                        host, port, bodyHash).getBytes(StandardCharsets.UTF_8))));
    }

}
