package it.gov.pagopa.tkm.ms.parretriever.client.external.amex.model.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestAmexParResponse {

    private AmexParResponse amexParResponse = new AmexParResponse("input", "par", Boolean.TRUE);

    @Test
    void createInstance_complete() {
        AmexParResponse response = new AmexParResponse("input", "par", Boolean.TRUE);

        boolean equals = amexParResponse.equals(response);
        assertTrue(equals);

        assertTrue(amexParResponse.canEqual(response));

        assertTrue(amexParResponse.hashCode() != 0);

        assertNotNull(amexParResponse.toString());
    }

}
