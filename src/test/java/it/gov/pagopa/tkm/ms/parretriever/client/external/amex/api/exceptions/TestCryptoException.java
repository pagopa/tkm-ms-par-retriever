package it.gov.pagopa.tkm.ms.parretriever.client.external.amex.api.exceptions;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.junit.jupiter.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class TestCryptoException {

    private CryptoException cryptoException;

    @Test
    void createInstance_complete() {
        cryptoException = new CryptoException("message", new Exception());
        assertNotNull(cryptoException);
    }

    @Test
    void createInstance_noException() {
        cryptoException = new CryptoException("message");
        assertNotNull(cryptoException);
    }

}
