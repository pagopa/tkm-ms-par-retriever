package it.gov.pagopa.tkm.ms.parretriever.client.amex.enums;

public enum NotificationType {
    DELETE("delete"), SUSPEND("suspend"), RESUME("resume");

    private final String type;

    NotificationType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
