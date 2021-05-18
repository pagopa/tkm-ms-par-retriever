package it.gov.pagopa.tkm.ms.parretriever.client.consent.model.response;

public enum ConsentEntityEnum {

    DENY,
    ALLOW,
    PARTIAL;

    public static ConsentEntityEnum toConsentEntityEnum(ConsentRequestEnum requestEnum) {
        return ConsentEntityEnum.valueOf(requestEnum.name());
    }

}
