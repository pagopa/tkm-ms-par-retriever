package it.gov.pagopa.tkm.ms.parretriever.client.amex.api.security.authentication;

import it.gov.pagopa.tkm.ms.parretriever.client.amex.api.configuration.ConfigurationProvider;

import java.util.Map;

public interface AuthProvider {

    /**
     * Set the configuration provider to be used in the generation of the authentication headers.
     * This provider will be retrieve the values for items such as API Key etc.
     *
     * @param provider Configuration provider
     */
    AuthProvider setConfiguration(ConfigurationProvider provider);

    Map<String, String> generateAuthHeaders(String requestPayload, String requestUrl, String httpMethod);

}
