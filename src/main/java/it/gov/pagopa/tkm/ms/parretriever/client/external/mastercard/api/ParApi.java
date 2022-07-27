package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api;

import com.google.gson.reflect.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.util.*;
import lombok.*;
import okhttp3.*;

import java.util.*;

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
