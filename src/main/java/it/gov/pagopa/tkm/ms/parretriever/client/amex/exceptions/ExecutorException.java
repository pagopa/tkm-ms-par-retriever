package it.gov.pagopa.tkm.ms.parretriever.client.amex.exceptions;

public class ExecutorException extends RuntimeException{
    public ExecutorException(String message) {
        super(message);
    }

    public ExecutorException(String message, Exception exception) {
        super(message, exception);
    }
}
