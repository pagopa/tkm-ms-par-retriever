package it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api;

import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.util.ApiClient;
import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.util.ApiException;
import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.util.ApiResponse;

import com.google.gson.reflect.TypeToken;

import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.model.MastercardParRequest;
import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.model.MastercardParResponse;
import lombok.*;
import okhttp3.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class ParApi {

    private final ApiClient localVarApiClient;

    public Call getParPostCall(MastercardParRequest mastercardParRequest) throws ApiException {
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

        return localVarApiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, mastercardParRequest, localVarHeaderParams);
    }

    private Call getParPostValidateBeforeCall(MastercardParRequest mastercardParRequest) throws ApiException {
        return getParPostCall(mastercardParRequest);
    }

    public MastercardParResponse getParPost(MastercardParRequest mastercardParRequest) throws ApiException {
        ApiResponse<MastercardParResponse> localVarResp = getParPostWithHttpInfo(mastercardParRequest);
        return localVarResp.getData();
    }

    public ApiResponse<MastercardParResponse> getParPostWithHttpInfo(MastercardParRequest mastercardParRequest) throws ApiException {
        Call localVarCall = getParPostValidateBeforeCall(mastercardParRequest);
        Type localVarReturnType = new TypeToken<MastercardParResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

}
