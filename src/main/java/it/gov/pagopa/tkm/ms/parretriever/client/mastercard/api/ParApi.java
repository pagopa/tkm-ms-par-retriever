package it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api;

import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.util.ApiClient;
import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.util.ApiException;
import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.util.ApiResponse;

import com.google.gson.reflect.TypeToken;

import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.model.ParRequest;
import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.model.ParResponse;
import lombok.*;
import okhttp3.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class ParApi {

    private final ApiClient localVarApiClient;

    public Call getParPostCall(ParRequest parRequest) throws ApiException {
        String localVarPath = "/getPaymentAccountReference";

        Map<String, String> localVarQueryParams = new HashMap<>();
        Map<String, String> localVarCollectionQueryParams = new HashMap<>();
        Map<String, String> localVarHeaderParams = new HashMap<>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        return localVarApiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, parRequest, localVarHeaderParams);
    }

    private Call getParPostValidateBeforeCall(ParRequest parRequest) throws ApiException {
        return getParPostCall(parRequest);
    }

    public ParResponse getParPost(ParRequest parRequest) throws ApiException {
        ApiResponse<ParResponse> localVarResp = getParPostWithHttpInfo(parRequest);
        return localVarResp.getData();
    }

    public ApiResponse<ParResponse> getParPostWithHttpInfo(ParRequest parRequest) throws ApiException {
        Call localVarCall = getParPostValidateBeforeCall(parRequest);
        Type localVarReturnType = new TypeToken<ParResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

}
