package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.util;

import com.mastercard.developer.interceptors.OkHttpOAuth1Interceptor;
import io.netty.internal.tcnative.SSLSession;
import it.gov.pagopa.tkm.ms.parretriever.constant.DefaultBeans;
import okhttp3.*;
import okhttp3.internal.connection.RealCall;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import sun.security.pkcs.PKCS8Key;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestApiClient {

    private ApiClient apiClient;

    private DefaultBeans defaultBeans;

    @BeforeEach
    void init() {
        defaultBeans = new DefaultBeans();
    }

    @Test
    void createApiClient_newInstance() {
        Interceptor interceptor = new OkHttpOAuth1Interceptor("consumerKey", new PKCS8Key());
        apiClient = new ApiClient(interceptor);
        assertNotNull(apiClient);
    }

    @Test
    void parameterToString_stringReturn() {
        createApiClient_newInstance();

        String emptyString = apiClient.parameterToString(null);
        assertEquals("", emptyString);

        String string = apiClient.parameterToString("ciao");
        assertEquals("ciao", string);

        Collection<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        String joinedString = apiClient.parameterToString(strings);
        assertEquals(String.join(",", strings), joinedString);
    }

    @Test
    void jsonMime_buildJson() {
        createApiClient_newInstance();

        assertFalse(apiClient.isJsonMime(null));

        assertTrue(apiClient.isJsonMime("*/*"));
    }

    @Test
    void headerAccept_stringReturn() {
        createApiClient_newInstance();

        String nullString = apiClient.selectHeaderAccept(new String[0]);
        assertNull(nullString);

        String[] mimeArray = new String[]{"*/*"};
        String mimeString = apiClient.selectHeaderAccept(mimeArray);
        assertEquals("*/*", mimeString);

        String[] arrayString = new String[]{"Hello", "World"};
        String arrayHeaders = apiClient.selectHeaderAccept(arrayString);
        assertEquals(String.join(",", arrayString), arrayHeaders);
    }

    @Test
    void headerContentType_stringReturn() {
        createApiClient_newInstance();

        String jsonEmpty = apiClient.selectHeaderContentType(new String[0]);
        assertEquals("application/json", jsonEmpty);

        String[] arrayMime = new String[]{"*/*"};
        String jsonMime = apiClient.selectHeaderContentType(arrayMime);
        assertEquals("application/json", jsonMime);

        String[] someMime = new String[]{"ciao", "*/*"};
        String mimeString = apiClient.selectHeaderContentType(someMime);
        assertEquals("*/*", mimeString);

        String[] arrayString = new String[]{"Hello", "World"};
        String arrayHeader = apiClient.selectHeaderContentType(arrayString);
        assertEquals("Hello", arrayHeader);
    }

    @Test
    void escapeString_urlValue() {
        createApiClient_newInstance();

        String url = apiClient.escapeString("ciao");
        assertNotNull(url);

        String str = new String("ciao".getBytes(StandardCharsets.UTF_8), StandardCharsets.US_ASCII);
        url = apiClient.escapeString(str);
        assertEquals(str, url);
    }

    @Test
    void deserialize_jsonResponse() throws ApiException, IOException {
        createApiClient_newInstance();

        Object deserialize = apiClient.deserialize(null, null);
        assertNull(deserialize);

        Response response = getResponse(null, 200);
        deserialize = apiClient.deserialize(response, String.class);
        assertNull(deserialize);

        response = getResponseHeader();
        Response eqResponse = getResponseHeader();
        deserialize = apiClient.deserialize(response, String.class);
        assertEquals(Objects.requireNonNull(eqResponse.body()).string(), deserialize.toString());

        response = getResponseHeader();
        Response finalResponse = response;
        assertThrows(ApiException.class, () -> apiClient.deserialize(finalResponse, Integer.class));
    }

    @Test
    void serialize_bodyResponse() throws ApiException {
        createApiClient_newInstance();

        RequestBody body = apiClient.serialize(defaultBeans.PARLESS_CARD_1, "application/json");
        assertNotNull(body);

        assertThrows(ApiException.class, () -> apiClient.serialize(defaultBeans.PARLESS_CARD_2, "ciao"));
    }

    @Test
    void buildCall_httpClient() throws ApiException {
        createApiClient_newInstance();

        Call call = apiClient.buildCall("https://", "sandbox.api.visa.com/v1/binRangeDetails",
                "get", new HashMap<>(), new HashMap<>(), new Object(), new HashMap<>());
        assertNotNull(call);
    }

    @Test
    void buildUrl_params() {
        createApiClient_newInstance();

        String url = apiClient.buildUrl("https://", "sandbox.api.visa.com/v1/binRangeDetails", null,
                null);
        assertNotNull(url);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("code", "200");
        queryParams.put("message", "OK");
        Map<String, String> collectionQueryParams = new HashMap<>();
        collectionQueryParams.put("par", "1111111111111");
        url = apiClient.buildUrl("https://", "sandbox.api.visa.com/v1/binRangeDetails", queryParams,
                collectionQueryParams);
        assertNotNull(url);

        queryParams.put("code", null);
        collectionQueryParams.put("par", null);
        url = apiClient.buildUrl("https://", "sandbox.api.visa.com/v1/binRangeDetails?", queryParams,
                collectionQueryParams);
        assertNotNull(url);
    }

    @Test
    void verifyOkHttpClient_property() {
        createApiClient_newInstance();

        OkHttpClient client = new OkHttpClient();
        apiClient.setHttpClient(client);
        assertEquals(client, apiClient.getHttpClient());
    }

    @Test
    void handleResponse_stringResponse() throws MalformedURLException, ApiException {
        createApiClient_newInstance();

        Response response = getResponse(null, 200);
        Object responseHandled = apiClient.handleResponse(response, String.class);
        assertNull(responseHandled);

        ResponseBody responseBody = getResponseBody();

        response = getResponse(responseBody, 200);
        Response finalResponse = response;
        assertDoesNotThrow(() -> apiClient.handleResponse(finalResponse, String.class));

        response = getResponse(null, 404);
        Response finalResponse1 = response;
        assertThrows(ApiException.class, () -> apiClient.handleResponse(finalResponse1, null));

        response = getResponse(responseBody, 404);
        Response finalResponse2 = response;
        assertThrows(ApiException.class, () -> apiClient.handleResponse(finalResponse2, String.class));
    }

    @NotNull
    private ResponseBody getResponseBody() {
        return ResponseBody.create("{\n" +
                "\"clientId\": \"0123456789012345678901234567999\",\n" +
                "\"correlatnId\": \"0123456789012345678901234567000\",\n" +
                "\"primaryAccount\": \"1234567898000000\",\n" +
                "}", MediaType.parse("application/json; charset=utf-8"));
    }

    @NotNull
    private Response getResponse(ResponseBody responseBody, int code) throws MalformedURLException {
        return new Response(
                new Request.Builder().url(new URL("https://sandbox.api.visa.com/v1/binRangeDetails")).build(),
                Protocol.HTTP_1_1, "response", code,
                new Handshake(TlsVersion.TLS_1_0, CipherSuite.TLS_AES_128_CCM_8_SHA256, new ArrayList<>(),
                        ArrayList::new),
                new Headers.Builder().build(), responseBody, null, null, null,
                1, 1, null);
    }

    private Response getResponseHeader() throws MalformedURLException {
        return new Response(
                new Request.Builder().url(new URL("https://sandbox.api.visa.com/v1/binRangeDetails")).build(),
                Protocol.HTTP_1_1, "response", 200,
                new Handshake(TlsVersion.TLS_1_0, CipherSuite.TLS_AES_128_CCM_8_SHA256, new ArrayList<>(),
                        ArrayList::new),
                new Headers.Builder().add("Content-Type", "ciao").build(), getResponseBody(),
                null, null, null,
                1, 1, null);
    }

}
