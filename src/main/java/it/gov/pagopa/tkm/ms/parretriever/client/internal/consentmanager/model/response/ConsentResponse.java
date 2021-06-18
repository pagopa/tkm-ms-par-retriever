package it.gov.pagopa.tkm.ms.parretriever.client.internal.consentmanager.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import it.gov.pagopa.tkm.constant.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.Instant;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsentResponse {

    private ConsentEntityEnum consent;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TkmDatetimeConstant.DATE_TIME_PATTERN, timezone = TkmDatetimeConstant.DATE_TIME_TIMEZONE)
    private Instant lastUpdateDate;

    private Set<CardServiceConsent> details;

}
