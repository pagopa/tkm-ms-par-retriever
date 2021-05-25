package it.gov.pagopa.tkm.ms.parretriever.client.amex.api.security.authentication;


import it.gov.pagopa.tkm.ms.parretriever.client.amex.api.configuration.ConfigurationProvider;

import java.util.UUID;


public abstract class BaseAuthProvider implements AuthProvider {

    protected ConfigurationProvider configProvider;


    public String getRequestUUID() {
        return UUID.randomUUID().toString();
    }

    @Override
    public AuthProvider setConfiguration(ConfigurationProvider provider) {
        this.configProvider = provider;
        return this;
    }


    public String getConfigurationValue(String key) {
        return this.configProvider.getValue(key);
    }

}
