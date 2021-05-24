package it.gov.pagopa.tkm.ms.parretriever.client.amex.tokenization;


import it.gov.pagopa.tkm.ms.parretriever.client.amex.models.ApiClientRequest;

public class StatusRequest extends ApiClientRequest {
    private final String tokenReferenceId;
    private final String STATUS_TARGET_URI = "/payments/digital/v2/tokens/%s/status";

    private StatusRequest(String tokenReferenceId) {
        this.tokenReferenceId = tokenReferenceId;
    }

    public static class StatusRequestBuilder {

        private String tokenReferenceId;

        public StatusRequestBuilder setTokenReferenceId(String tokenReferenceId) {
            this.tokenReferenceId = tokenReferenceId;
            return this;
        }

        public StatusRequest createStatusRequest() {
            return new StatusRequest(tokenReferenceId);
        }
    }

    @Override
    public String toJson(String kid, String aesKey) {
        return null;
    }

    @Override
    public String getUri() {
        return String.format(STATUS_TARGET_URI, tokenReferenceId);
    }

    @Override
    public String getHttpAction() {
        return "GET";
    }
}
