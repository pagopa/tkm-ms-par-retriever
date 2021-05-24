package it.gov.pagopa.tkm.ms.parretriever.constant;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCodeEnum {

    REQUEST_VALIDATION_FAILED("P1000", "Request validation failed, check for errors in the request body or headers");

    @Getter
    private final String errorCode;

    @Getter
    private final String description;

}
