package it.gov.pagopa.tkm.ms.parretriever.client.amex.security;


import it.gov.pagopa.tkm.ms.parretriever.client.amex.configuration.ConfigurationProvider;
import it.gov.pagopa.tkm.ms.parretriever.client.amex.security.authentication.AuthProvider;

public interface AuthProviderBuilder {

    AuthProviderBuilder setConfiguration(ConfigurationProvider provider);

    AuthProvider build();

}
