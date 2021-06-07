package it.gov.pagopa.tkm.ms.parretriever.client.internal.consentmanager.model.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceConsent {

    private ConsentRequestEnum consent;

    private ServiceEnum service;

}
