package it.gov.pagopa.tkm.ms.parretriever.client.amex.tokenization;

import it.gov.pagopa.tkm.ms.parretriever.client.amex.models.ApiClientRequest;

public class MetaDataRequest extends ApiClientRequest {

    private final String tokenReferenceId;
    private final String METADATA_TARGET_URI = "/payments/digital/v2/tokens/%s/metadata";

    private MetaDataRequest(String tokenReferenceId) {
        this.tokenReferenceId = tokenReferenceId;
    }

    public static class MetaDataRequestBuilder {

        private String tokenReferenceId;

        public MetaDataRequestBuilder setTokenReferenceId(String tokenReferenceId) {
            this.tokenReferenceId = tokenReferenceId;
            return this;
        }

        public MetaDataRequest createMetaDataRequest() {
            return new MetaDataRequest(tokenReferenceId);
        }
    }

    @Override
    public String toJson(String kid, String aesKey) {
        return null;
    }

    @Override
    public String getUri() {
        return String.format(METADATA_TARGET_URI, tokenReferenceId);
    }

    @Override
    public String getHttpAction() {
        return "GET";
    }
}
