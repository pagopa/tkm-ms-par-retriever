package it.gov.pagopa.tkm.ms.parretriever.client.amex.enums;

public enum AccountInputMethod {
    ON_FILE("On File"), USER_INPUT("User Input");

    private final String value;

    AccountInputMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
