package it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.util;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ApiResponse<T> {

    final private int statusCode;

    final private Map<String, List<String>> headers;

    final private T data;

}
