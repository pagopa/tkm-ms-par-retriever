package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.util;

import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
public class ApiResponse<T> {

    final private int statusCode;

    final private Map<String, List<String>> headers;

    final private T data;

}
