package it.gov.pagopa.tkm.ms.parretriever.client.amex.security.authentication;



import it.gov.pagopa.tkm.ms.parretriever.client.amex.configuration.ConfigurationKeys;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
public class ApiKeyAuthProvider extends BaseAuthProvider {
    /**
     * Generates the minimal headers required for authentication. The values for API Key header are derived by the ConfigurationProvider supplied.
     * A GUID will be supplied for the request id header.
     */
    @Override
    public Map<String, String> generateAuthHeaders(String reqPayload, String requestUrl, String httpMethod) {
        Map<String, String> headers = new Hashtable<String, String>();
        headers.put(AuthHeaderNames.X_AMEX_API_KEY, getConfigurationValue(ConfigurationKeys.CLIENT_KEY));
        headers.put(AuthHeaderNames.X_AMEX_REQUEST_ID, getRequestUUID());
        return Collections.unmodifiableMap(headers);
    }

}
