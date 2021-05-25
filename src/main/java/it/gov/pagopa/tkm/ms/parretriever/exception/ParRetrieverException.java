package it.gov.pagopa.tkm.ms.parretriever.exception;

import it.gov.pagopa.tkm.ms.parretriever.constant.ErrorCodeEnum;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
public class ParRetrieverException extends RuntimeException {

    private ErrorCodeEnum errorCode;

    public ParRetrieverException(ErrorCodeEnum ec) {
        super(ec.getErrorCode() + " - " + ec.getDescription());
        errorCode = ec;
    }

}
