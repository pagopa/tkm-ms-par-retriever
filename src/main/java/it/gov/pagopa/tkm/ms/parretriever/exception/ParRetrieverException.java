package it.gov.pagopa.tkm.ms.parretriever.exception;

import it.gov.pagopa.tkm.ms.parretriever.constant.ErrorCodeEnum;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
public class ParRetrieverException extends RuntimeException {

    private ErrorCodeEnum errorCodeEnum;

    public ParRetrieverException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getErrorCode() + " - " + errorCodeEnum.getDescription());
        setErrorCodeEnum(errorCodeEnum);
    }

}
