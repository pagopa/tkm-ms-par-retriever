package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.util;

import com.google.gson.Gson;
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
        assertTrue(json.isLenientOnJson());

        Object body = json.deserialize("body", JSON.class.getGenericSuperclass());
        assertNotNull(body);
    }

    @Test
    void verifyProperties_setterGetter() {
        Gson gson = new Gson();
        json.setGson(gson);
        assertEquals(gson, json.getGson());
    }

    @Test
    void createInstance_complete() {
        Gson gson = new Gson();

        json.setGson(gson);
        json.setLenientOnJson(true);

        JSON eqObj = new JSON();
        eqObj.setLenientOnJson(true);
        eqObj.setGson(gson);

        boolean equals = json.equals(eqObj);
        assertTrue(equals);

        assertTrue(json.hashCode() != 0);

        assertNotNull(json.toString());

        assertTrue(json.canEqual(eqObj));
    }

}
