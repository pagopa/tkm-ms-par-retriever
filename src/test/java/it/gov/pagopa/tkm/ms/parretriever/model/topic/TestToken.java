package it.gov.pagopa.tkm.ms.parretriever.model.topic;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.junit.jupiter.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class TestToken {

    private Token token;

    @Test
    void checkProperties_setterGetter() {
        token = new Token();

        token.setHToken("htoken");
        assertEquals("htoken", token.getHToken());

        token.setToken("token");
        assertEquals("token", token.getToken());
    }

    @Test
    void createInstance_complete() {
        token = new Token("token", "htoken");
        Token tkn = new Token("token", "htoken");

        boolean equals = token.equals(tkn);
        assertTrue(equals);

        assertTrue(token.canEqual(tkn));

        assertTrue(token.hashCode() != 0);

        assertNotNull(token.toString());
    }

}
