package it.gov.pagopa.tkm.ms.parretriever.client.amex.api.security.authentication;

public class HmacAuthBuilder extends BaseAuthBuilder {

    private HmacAuthBuilder() {
        super();
    }

    public static HmacAuthBuilder getBuilder() {
        return new HmacAuthBuilder();
    }

    public AuthProvider build() {
        HmacAuthProvider provider = new HmacAuthProvider();
        provider.setConfiguration(super.getConfiguration());

        return provider;
    }

}
