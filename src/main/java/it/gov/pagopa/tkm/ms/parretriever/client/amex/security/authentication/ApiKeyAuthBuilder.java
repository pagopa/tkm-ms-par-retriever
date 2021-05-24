package it.gov.pagopa.tkm.ms.parretriever.client.amex.security.authentication;

public class ApiKeyAuthBuilder extends BaseAuthBuilder {

    private ApiKeyAuthBuilder(){
        super();
    }

    public static final ApiKeyAuthBuilder getBuilder(){
        return new ApiKeyAuthBuilder();
    }


    public AuthProvider build(){
        ApiKeyAuthProvider provider = new ApiKeyAuthProvider();
        provider.setConfiguration(super.getConfiguration());
        return  provider;
    }
}
