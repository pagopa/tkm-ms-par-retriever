package it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.model.response;

import lombok.*;

import java.io.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParlessCardToken implements Serializable {

    private String token;

    private String htoken;

}