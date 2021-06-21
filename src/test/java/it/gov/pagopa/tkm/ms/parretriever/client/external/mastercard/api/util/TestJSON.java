package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestJSON {

    @InjectMocks
    private JSON json;

    @Test
    void deserialize_lenientBody() {
        json.setLenientOnJson(true);
        Object body = json.deserialize("body", JSON.class.getGenericSuperclass());
        assertNotNull(body);
    }

}
