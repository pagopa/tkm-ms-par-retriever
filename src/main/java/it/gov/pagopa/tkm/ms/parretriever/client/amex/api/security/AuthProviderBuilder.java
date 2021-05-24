package it.gov.pagopa.tkm.ms.parretriever.client.amex.api.security;


import it.gov.pagopa.tkm.ms.parretriever.client.amex.api.configuration.ConfigurationProvider;
import it.gov.pagopa.tkm.ms.parretriever.client.amex.api.security.authentication.AuthProvider;

public interface AuthProviderBuilder {

    AuthProviderBuilder setConfiguration(ConfigurationProvider provider);

    AuthProvider build();

}
