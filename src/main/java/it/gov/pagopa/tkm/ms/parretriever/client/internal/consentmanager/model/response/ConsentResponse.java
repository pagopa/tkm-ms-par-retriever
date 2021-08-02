package it.gov.pagopa.tkm.ms.parretriever.client.internal.consentmanager.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import it.gov.pagopa.tkm.constant.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.Instant;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsentResponse {

    private ConsentEntityEnum consent;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "Europe/Rome")
    private Instant lastUpdateDate;

    private Set<CardServiceConsent> details;

}
