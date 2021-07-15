package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard;

import com.fasterxml.jackson.databind.*;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.util.*;
import it.gov.pagopa.tkm.ms.parretriever.constant.*;
import okhttp3.mockwebserver.*;
import org.apache.commons.io.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.*;
import org.springframework.test.util.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestMastercardClient {

    @InjectMocks
    private MastercardClient mastercardClient;

    private static MockWebServer mockServer;

    private DefaultBeans testBeans;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    CircuitBreakerRegistry circuitBreakerRegistry;


    @BeforeEach
    void init() throws Exception {
        testBeans = new DefaultBeans();
        ReflectionTestUtils.setField(mastercardClient, "privateDecryptionKey", FileUtils.readFileToString(new File("src/test/resources/client_private_key_test.key")));
        ReflectionTestUtils.setField(mastercardClient, "publicEncryptionKey", FileUtils.readFileToString(new File("src/test/resources/client_public_cert_test.crt")));
        ReflectionTestUtils.setField(mastercardClient, "signingKeyCert", new ClassPathResource("public_cert_test.p12"));
        ReflectionTestUtils.setField(mastercardClient, "signingKeyPassword", "password");
        ReflectionTestUtils.setField(mastercardClient, "consumerKey", "TEST_CONSUMER_KEY");
        ReflectionTestUtils.setField(mastercardClient, "retrieveParUrl", "http://localhost:" + mockServer.getPort());
        //Skipping circuit-side encryption, cannot mock
        ReflectionTestUtils.setField(mastercardClient, "api", new ParApi(new ApiClient()));
    }

    @BeforeAll
    static void setUp() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockServer.shutdown();
    }

    @Test
    void init_api() {
        assertDoesNotThrow(() -> mastercardClient.init());
    }

    @Test
    void givenPan_assertApiGetsCalled() throws Exception {
        MockResponse mockResponse = new MockResponse().setBody(mapper.writeValueAsString(testBeans.MASTERCARD_PAR_RESPONSE));
        mockServer.enqueue(mockResponse);
        String actualPar = mastercardClient.getPar(testBeans.PAN_1);
        assertEquals(testBeans.PAR_1, actualPar);
    }


    @Test
    public void shouldDecorateSupplierAndReturnWithSuccess() throws Exception {
        //CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("externalCardCircuitClientBreaker");
        //
        System.out.println("\nBEFORE....... circuitBreaker ");

        //CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
        CircuitBreaker circuitBssreaker = circuitBreakerRegistry.circuitBreaker("externalCardCircuitClientBreaker");

        System.out.println("circuitBreakerRegistry.getAllCircuitBreakers().size() "
                + circuitBreakerRegistry.getAllCircuitBreakers().size());

        final CircuitBreaker backendC = circuitBreakerRegistry.getAllCircuitBreakers()
                .filter(circuitBreaker -> circuitBreaker.getName().equalsIgnoreCase("externalCardCircuitClientBreaker"))
                .get();

      //  CircuitBreaker.Metrics metrics = circuitBreaker.getMetrics();
        System.out.println("\nAFTER................ circuitBreaker ");
        Boolean nn = backendC==null;
        System.out.println("\nAFTER................ circuitBreaker IS NULL" + nn);

        System.out.println("\nAFTER................ circuitBreaker.----- ");

        /*assertEquals(metrics.getNumberOfBufferedCalls(), 0);
        MockResponse mockResponse = new MockResponse().setBody(mapper.writeValueAsString(testBeans.MASTERCARD_PAR_RESPONSE));
        mockServer.enqueue(mockResponse);
        Supplier<String> supplier = circuitBreaker
                .decorateSupplier(mastercardClient.getPar(testBeans.PAN_1)::toString);

        String result = supplier.get();

        assertEquals(result,testBeans.PAR_1);
        assertEquals(metrics.getNumberOfBufferedCalls(),1);
        assertEquals(metrics.getNumberOfFailedCalls(),0);
        assertEquals(metrics.getNumberOfSuccessfulCalls(),1);
        assertEquals(metrics.getNumberOfBufferedCalls(),1); */

    }

    @Test
    public void shouldDecorateSupplierAndReturnWithSuccess2() throws Exception {
        System.out.println("\n..........|||| ...... circuitBreaker IS NULL? ");

    }

}
