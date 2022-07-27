package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.util;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class TestApiResponse {

    @InjectMocks
    private ApiResponse<Object> response = new ApiResponse<>(0, new HashMap<>(), new Object());

    @Test
    void checkProperties_setterGetter() {
        assertEquals(0, response.getStatusCode());
        assertNotNull(response.getHeaders());
    }

    @Test
    void verifyData_objectMethods() {
        int statusCode = 10;

        Map<String, List<String>> headers = new HashMap<>();
        List<String> values = new ArrayList<>();
        values.add("200");
        values.add("OK");
        headers.put("response", values);

        Object data = new Object();

        response = new ApiResponse<>(statusCode, headers, data);

        ApiResponse<Object> objectApiResponse = new ApiResponse<>(statusCode, headers, data);

        boolean equals = response.equals(objectApiResponse);
        assertTrue(equals);

        objectApiResponse = new ApiResponse<>(10, new HashMap<String, List<String>>() {{
            put("response", Arrays.asList("200", "OK"));
        }}, new Object());

        boolean eq = response.equals(objectApiResponse);
        assertFalse(eq);

        assertTrue(response.hashCode() != 0);

        assertNotNull(response.toString());

        assertTrue(response.canEqual(objectApiResponse));
    }

}
