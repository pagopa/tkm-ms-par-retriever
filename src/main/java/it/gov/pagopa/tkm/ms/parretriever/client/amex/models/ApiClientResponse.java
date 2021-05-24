package it.gov.pagopa.tkm.ms.parretriever.client.amex.models;


import it.gov.pagopa.tkm.ms.parretriever.client.amex.utils.EncryptionUtility;
import it.gov.pagopa.tkm.ms.parretriever.client.amex.utils.JsonUtility;
import org.apache.http.Header;

import java.util.Map;

public class ApiClientResponse {
    private final Header[] headers;
    private final Map responseMap;
    private final String body;

    public ApiClientResponse(Header[] headers, String body) {
        this.headers = headers;

        if (body != null && !"".equals(body)) {
            body = JsonUtility.getInstance().prettyString(body);
        } else {
            body = null;
        }

        responseMap = body == null ? null : JsonUtility.getInstance().getObject(body, Map.class);

        this.body = body;
    }

    //Get a particular field from the body of the response
    public Object getField(String key) {
        if(responseMap != null) {
            return responseMap.get(key);
        }

        return null;
    }

    public String getHeader(String key) {
        if(headers != null) {
            for (Header header : headers) {
                if (key.equals(header.getName())) {
                    return header.getValue();
                }
            }
        }

        return null;
    }

    public String getDecryptedField(String keyStr, String field) {
        Object encryptedData = responseMap.get(field);

        if (encryptedData instanceof String) {
            return EncryptionUtility.getInstance().decrypt(keyStr, (String)encryptedData);
        }

        return null;
    }

    public String toJson() {
        return body;
    }

}
