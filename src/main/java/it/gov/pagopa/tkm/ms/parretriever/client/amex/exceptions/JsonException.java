package it.gov.pagopa.tkm.ms.parretriever.client.amex.exceptions;

public class JsonException extends RuntimeException {

    public JsonException(String message, Exception exception) {
        super(message, exception);
    }
}
