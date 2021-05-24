package it.gov.pagopa.tkm.ms.parretriever.client.amex.security.authentication;


import it.gov.pagopa.tkm.ms.parretriever.client.amex.configuration.ConfigurationProvider;
import it.gov.pagopa.tkm.ms.parretriever.client.amex.security.AuthProviderBuilder;

public abstract class BaseAuthBuilder implements AuthProviderBuilder {

    private ConfigurationProvider configProvider;

    @Override
    public AuthProviderBuilder setConfiguration(ConfigurationProvider provider) {
        this.configProvider = provider;
        return this;
    }


    protected ConfigurationProvider getConfiguration() {
        return this.configProvider;
    }




}
