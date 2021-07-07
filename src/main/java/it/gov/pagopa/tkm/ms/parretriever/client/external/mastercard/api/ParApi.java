package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api;

import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.util.ApiClient;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.util.ApiException;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.util.ApiResponse;

import com.google.gson.reflect.TypeToken;

import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model.MastercardParRequest;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model.MastercardParResponse;
import lombok.*;
import okhttp3.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class ParApi {

    private final ApiClient localVarApiClient;

    public Call getParPostCall(String parEndpoint, MastercardParRequest mastercardParRequest) throws ApiException {
        Map<String, String> localVarHeaderParams = new HashMap<>();

        final String localVarAccept = localVarApiClient.selectHeaderAccept(new String[]{"application/json"});
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        localVarHeaderParams.put("Content-Type",
                localVarApiClient.selectHeaderContentType(new String[]{"application/json"}));

        return localVarApiClient.buildCall(parEndpoint, "/getPaymentAccountReference", "POST",
                new HashMap<>(), new HashMap<>(), mastercardParRequest, localVarHeaderParams);
    }

    private Call getParPostValidateBeforeCall(String parEndpoint, MastercardParRequest mastercardParRequest)
            throws ApiException {
        return getParPostCall(parEndpoint, mastercardParRequest);
    }

    public MastercardParResponse getParPost(String parEndpoint, MastercardParRequest mastercardParRequest)
            throws ApiException {
        return getParPostWithHttpInfo(parEndpoint, mastercardParRequest).getData();
    }

    public ApiResponse<MastercardParResponse> getParPostWithHttpInfo(String parEndpoint,
                                                                     MastercardParRequest mastercardParRequest)
            throws ApiException {
        return localVarApiClient.execute(getParPostValidateBeforeCall(parEndpoint, mastercardParRequest),
                new TypeToken<MastercardParResponse>() {
                }.getType());
    }

}
