package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestApiResponse {

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

        assertTrue(response.hashCode() != 0);

        assertNotNull(response.toString());

        assertTrue(response.canEqual(objectApiResponse));
    }

}
