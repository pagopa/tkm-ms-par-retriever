package it.gov.pagopa.tkm.ms.parretriever.client.external.amex.api.security;

import it.gov.pagopa.tkm.ms.parretriever.client.external.amex.api.configuration.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.amex.api.security.authentication.*;

public interface AuthProviderBuilder {

    AuthProviderBuilder setConfiguration(ConfigurationProvider provider);

    AuthProvider build();

}
