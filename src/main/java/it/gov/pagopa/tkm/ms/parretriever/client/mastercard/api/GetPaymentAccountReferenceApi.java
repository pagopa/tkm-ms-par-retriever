package it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api;

import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.util.ApiCallback;
import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.util.ApiClient;
import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.util.ApiException;
import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.util.ApiResponse;
import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.util.Pair;

import com.google.gson.reflect.TypeToken;

import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.model.ParRequest;
import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.model.ParResponse;
import lombok.*;
import okhttp3.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class GetPaymentAccountReferenceApi {

    private ApiClient localVarApiClient;

    public Call getParPostCall(ParRequest parRequest, final ApiCallback _callback) throws ApiException {
        String localVarPath = "/getPaymentAccountReference";

        List<Pair> localVarQueryParams = new ArrayList<>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<>();
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

        return localVarApiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, parRequest, localVarHeaderParams, _callback);
    }

    @SuppressWarnings("rawtypes")
    private Call getParPostValidateBeforeCall(ParRequest parRequest, final ApiCallback _callback) throws ApiException {
        return getParPostCall(parRequest, _callback);
    }

    public ParResponse getParPost(ParRequest parRequest) throws ApiException {
        ApiResponse<ParResponse> localVarResp = getParPostWithHttpInfo(parRequest);
        return localVarResp.getData();
    }

    public ApiResponse<ParResponse> getParPostWithHttpInfo(ParRequest parRequest) throws ApiException {
        Call localVarCall = getParPostValidateBeforeCall(parRequest, null);
        Type localVarReturnType = new TypeToken<ParResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

}
