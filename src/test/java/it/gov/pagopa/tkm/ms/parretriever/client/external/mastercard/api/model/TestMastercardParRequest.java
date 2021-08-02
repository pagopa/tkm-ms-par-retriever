package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.CheckedFunction0;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import io.vavr.control.Try;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestMastercardParRequest {

    private MastercardParRequest request;

    @Test
    void verifyProperties_setterGetter() {
        request = new MastercardParRequest("requestId",
                new ParRequestEncryptedPayload(new ParRequestEncryptedData("accountNumber",
                        "dataValidUntilTimestamp")));

        String newRequestId = "0569664126";
        request.setRequestId(newRequestId);
        assertEquals(newRequestId, request.getRequestId());

        ParRequestEncryptedData parRequestEncryptedData = new ParRequestEncryptedData("3215589648565967",
                LocalDateTime.now().toString());
        ParRequestEncryptedPayload parRequestEncryptedPayload = new ParRequestEncryptedPayload(parRequestEncryptedData);
        request.setEncryptedPayload(parRequestEncryptedPayload);
        assertEquals(parRequestEncryptedPayload, request.getEncryptedPayload());
    }

    @Test
    void createInstance_complete() {
        request = new MastercardParRequest("requestId",
                new ParRequestEncryptedPayload(new ParRequestEncryptedData("accountNumber",
                        "dataValidUntilTimestamp")));

        MastercardParRequest mastercardParRequest = new MastercardParRequest("requestId",
                new ParRequestEncryptedPayload(new ParRequestEncryptedData("accountNumber",
                        "dataValidUntilTimestamp")));
        boolean equals = request.equals(mastercardParRequest);
        assertTrue(equals);

        assertTrue(request.hashCode()!=0);

        assertNotNull(request.toString());

        assertTrue(request.canEqual(mastercardParRequest));
    }



    @Test
    public void shouldInvokeRecoverFunction() {
        // tag::shouldInvokeRecoverFunction[]
        //CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("testName");
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("testName");
        // When I decorate my function and invoke the decorated function
        CheckedFunction0<String> checkedSupplier = circuitBreaker.decorateCheckedSupplier(() -> {
            throw new RuntimeException("BAM!");
        });

        Try<String> result = Try.of(checkedSupplier)
                .recover(throwable -> "Hello Recovery");

        // Then the function should be a success, because the exception could be recovered
        assertThat(result.isSuccess()).isTrue();
        // and the result must match the result of the recovery function.
        assertThat(result.get()).isEqualTo("Hello Recovery");
        // end::shouldInvokeRecoverFunction[]
    }



  /*  @Test
    public void should_search_and_fallback_when_ResourceAccessException_is_thrown() {
        // prepare
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class), eq(SEARCH_VALUE)))
                .thenThrow(ResourceAccessException.class);

        String expectedResult = "expected result when fallback is called";

        // action
        String actualResult = client.search(SEARCH_VALUE);

        // assertion
        verify(client).fallback(eq(SEARCH_VALUE), any(ResourceAccessException.class));
        assertThat(actualResult, is(expectedResult));
    } */


}
