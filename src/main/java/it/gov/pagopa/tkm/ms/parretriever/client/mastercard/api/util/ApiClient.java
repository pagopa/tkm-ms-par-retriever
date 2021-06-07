package it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.util;

import lombok.*;
import okhttp3.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.*;

public class ApiClient {

    @Getter
    @Setter
    private OkHttpClient httpClient;

    private final JSON json;

    public ApiClient(Interceptor... interceptors) {
        json = new JSON();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        for (Interceptor interceptor : interceptors) {
            builder.addInterceptor(interceptor);
        }
        httpClient = builder.build();
    }

    public String parameterToString(Object param) {
        if (param == null) {
            return "";
        } else if (param instanceof Collection) {
            StringBuilder b = new StringBuilder();
            for (Object o : (Collection) param) {
                if (b.length() > 0) {
                    b.append(",");
                }
                b.append(o);
            }
            return b.toString();
        } else {
            return String.valueOf(param);
        }
    }

    public boolean isJsonMime(String mime) {
        String jsonMime = "(?i)^(application/json|[^;/ \t]+/[^;/ \t]+[+]json)[ \t]*(;.*)?$";
        return mime != null && (mime.matches(jsonMime) || mime.equals("*/*"));
    }

    public String selectHeaderAccept(String[] accepts) {
        if (accepts.length == 0) {
            return null;
        }
        for (String accept : accepts) {
            if (isJsonMime(accept)) {
                return accept;
            }
        }
        return String.join(",", accepts);
    }

    public String selectHeaderContentType(String[] contentTypes) {
        if (contentTypes.length == 0 || contentTypes[0].equals("*/*")) {
            return "application/json";
        }
        for (String contentType : contentTypes) {
            if (isJsonMime(contentType)) {
                return contentType;
            }
        }
        return contentTypes[0];
    }

    public String escapeString(String str) {
        try {
            return URLEncoder.encode(str, "utf8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialize(Response response, Type returnType) throws ApiException {
        if (response == null || returnType == null) {
            return null;
        }
        String respBody;
        try {
            ResponseBody responseBody = response.body();
            if (responseBody != null)
                respBody = responseBody.string();
            else
                respBody = null;
        } catch (IOException e) {
            throw new ApiException(e);
        }

        if (respBody == null || "".equals(respBody)) {
            return null;
        }

        String contentType = response.headers().get("Content-Type");
        if (contentType == null) {
            // ensuring a default content type
            contentType = "application/json";
        }
        if (isJsonMime(contentType)) {
            return json.deserialize(respBody, returnType);
        } else if (returnType.equals(String.class)) {
            // Expecting string, return the raw response body.
            return (T) respBody;
        } else {
            throw new ApiException(
                    "Content type \"" + contentType + "\" is not supported for type: " + returnType,
                    response.code(),
                    response.headers().toMultimap(),
                    respBody);
        }
    }

    public RequestBody serialize(Object obj, String contentType) throws ApiException {
        if (isJsonMime(contentType) && obj != null) {
            String content = json.serialize(obj);
            return RequestBody.create(content, MediaType.parse(contentType));
        } else {
            throw new ApiException("Content type \"" + contentType + "\" is not supported");
        }
    }

    public <T> ApiResponse<T> execute(Call call, Type returnType) throws ApiException {
        try {
            Response response = call.execute();
            T data = handleResponse(response, returnType);
            return new ApiResponse<>(response.code(), response.headers().toMultimap(), data);
        } catch (IOException e) {
            throw new ApiException(e);
        }
    }

    public <T> T handleResponse(Response response, Type returnType) throws ApiException {
        if (response.isSuccessful()) {
            if (returnType == null || response.code() == 204) {
                // returning null if the returnType is not defined,
                // or the status code is 204 (No Content)
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    try {
                        responseBody.close();
                    } catch (Exception e) {
                        throw new ApiException(response.message(), e, response.code(), response.headers().toMultimap());
                    }
                }
                return null;
            } else {
                return deserialize(response, returnType);
            }
        } else {
            String respBodyString = null;
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                try {
                    respBodyString = responseBody.string();
                } catch (IOException e) {
                    throw new ApiException(response.message(), e, response.code(), response.headers().toMultimap());
                }
            }
            throw new ApiException(response.message(), response.code(), response.headers().toMultimap(), respBodyString);
        }
    }

    public Call buildCall(String parEndpoint, String path, String method, Map<String, String> queryParams, Map<String, String> collectionQueryParams, Object body, Map<String, String> headerParams) throws ApiException {
        Request request = buildRequest(parEndpoint, path, method, queryParams, collectionQueryParams, body, headerParams);

        return httpClient.newCall(request);
    }

    public Request buildRequest(String parEndpoint, String path, String method, Map<String, String> queryParams, Map<String, String> collectionQueryParams, Object body, Map<String, String> headerParams) throws ApiException {
        final String url = buildUrl(parEndpoint, path, queryParams, collectionQueryParams);
        final Request.Builder reqBuilder = new Request.Builder().url(url);

        String contentType = headerParams.get("Content-Type");
        // ensuring a default content type
        if (contentType == null) {
            contentType = "application/json";
        }
        RequestBody reqBody = serialize(body, contentType);
        return reqBuilder.method(method, reqBody).build();
    }

    public String buildUrl(String parEndpoint, String path, Map<String, String> queryParams, Map<String, String> collectionQueryParams) {
        final StringBuilder url = new StringBuilder();
        url.append(parEndpoint).append(path);

        if (queryParams != null && !queryParams.isEmpty()) {
            // support (constant) query string in `path`, e.g. "/posts?draft=1"
            String prefix = path.contains("?") ? "&" : "?";
            for (Map.Entry<String, String> param : queryParams.entrySet()) {
                if (param.getValue() != null) {
                    if (prefix != null) {
                        url.append(prefix);
                        prefix = null;
                    } else {
                        url.append("&");
                    }
                    String value = parameterToString(param.getValue());
                    url.append(escapeString(param.getKey())).append("=").append(escapeString(value));
                }
            }
        }

        if (collectionQueryParams != null && !collectionQueryParams.isEmpty()) {
            String prefix = url.toString().contains("?") ? "&" : "?";
            for (Map.Entry<String, String> param : collectionQueryParams.entrySet()) {
                if (param.getValue() != null) {
                    if (prefix != null) {
                        url.append(prefix);
                        prefix = null;
                    } else {
                        url.append("&");
                    }
                    String value = parameterToString(param.getValue());
                    url.append(escapeString(param.getKey())).append("=").append(value);
                }
            }
        }

        return url.toString();
    }

}
