package it.gov.pagopa.tkm.ms.parretriever.config;

import it.gov.pagopa.tkm.ms.parretriever.constant.ErrorCodeEnum;
import it.gov.pagopa.tkm.ms.parretriever.exception.ParRetrieverException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class ErrorHandler {

    @ExceptionHandler(ParRetrieverException.class)
    public ResponseEntity<ErrorCodeEnum> handleParRetrieverException(ParRetrieverException parRetrieverException) {
        log.error(parRetrieverException.getMessage());
        return ResponseEntity.badRequest().body(parRetrieverException.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception e) {
        log.error(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
