package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestApiResponse {

    @InjectMocks
    private final ApiResponse<Object> response = new ApiResponse<>(0, new HashMap<>(), new Object());

    @Test
    void checkProperties_setterGetter() {
        assertEquals(0, response.getStatusCode());
        assertNotNull(response.getHeaders());
    }

}
