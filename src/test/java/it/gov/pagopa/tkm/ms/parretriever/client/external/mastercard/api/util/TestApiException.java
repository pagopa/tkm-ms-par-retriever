package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.util;

import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestApiException {

    private ApiException apiException;

    @Test
    void createInstance_throwable() {
        apiException = new ApiException(new Throwable());
        assertNotNull(apiException);
    }

    @Test
    void createInstance_message() {
        apiException = new ApiException("message");
        assertNotNull(apiException);
    }

    @Test
    void createInstance_complete() {
        Map<String, List<String>> responseHeaders = getStringListMap();

        apiException = new ApiException("message", new Throwable(), 10, responseHeaders, "PAR");
        assertNotNull(apiException);
        assertEquals(10, apiException.getCode());
        assertEquals(responseHeaders, apiException.getResponseHeaders());
        assertEquals("PAR", apiException.getResponseBody());
    }

    @NotNull
    private Map<String, List<String>> getStringListMap() {
        Map<String, List<String>> responseHeaders = new HashMap<>();
        List<String> response = new ArrayList<>();
        response.add("200");
        response.add("OK");
        responseHeaders.put("response", response);
        return responseHeaders;
    }

    @Test
    void createInstance_noThrowable() {
        Map<String, List<String>> responseHeaders = getStringListMap();

        apiException = new ApiException("message", 15, responseHeaders, "response");
        assertNotNull(apiException);
        assertEquals(15, apiException.getCode());
        assertEquals(responseHeaders, apiException.getResponseHeaders());
        assertEquals("response", apiException.getResponseBody());
    }

    @Test
    void createInstance_noResponseBody() {
        Map<String, List<String>> responseHeaders = getStringListMap();

        apiException = new ApiException("message", new Throwable(), 20, responseHeaders);
        assertNotNull(apiException);
        assertEquals(20, apiException.getCode());
        assertEquals(responseHeaders, apiException.getResponseHeaders());
    }

}
