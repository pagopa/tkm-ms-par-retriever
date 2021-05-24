package it.gov.pagopa.tkm.ms.parretriever.client.amex.models;

public abstract class ApiClientRequest {
    abstract public String toJson(String kid, String aesKey);
    abstract public String getUri();
    abstract public String getHttpAction();

    public String toJson() {
        return toJson(null, null);
    }
}
