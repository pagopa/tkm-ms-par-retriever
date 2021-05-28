package it.gov.pagopa.tkm.ms.parretriever.client.amex.api.exceptions;

public class CryptoException extends RuntimeException {

    public CryptoException(String message, Exception exception) {
        super(message, exception);
    }

    public CryptoException(String message) {
        super(message);
    }

}
