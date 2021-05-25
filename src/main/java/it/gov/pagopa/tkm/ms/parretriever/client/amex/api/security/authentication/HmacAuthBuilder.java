package it.gov.pagopa.tkm.ms.parretriever.client.amex.api.security.authentication;

public class HmacAuthBuilder extends BaseAuthBuilder {

    private HmacAuthProvider provider;

    private HmacAuthBuilder() {
        super();
        this.provider = new HmacAuthProvider();
    }


    public static final HmacAuthBuilder getBuilder() {
        return new HmacAuthBuilder();
    }


    public AuthProvider build() {
        HmacAuthProvider provider = new HmacAuthProvider();
        provider.setConfiguration(super.getConfiguration());

        return provider;
    }

}
