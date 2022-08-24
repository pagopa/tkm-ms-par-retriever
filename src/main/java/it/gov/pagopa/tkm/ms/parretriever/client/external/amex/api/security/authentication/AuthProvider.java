package it.gov.pagopa.tkm.ms.parretriever.client.external.amex.api.security.authentication;

import it.gov.pagopa.tkm.ms.parretriever.client.external.amex.api.configuration.*;

import java.util.*;

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
