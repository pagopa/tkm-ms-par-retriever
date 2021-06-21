package it.gov.pagopa.tkm.ms.parretriever.client.external.amex.api.exceptions;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestCryptoException {

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
