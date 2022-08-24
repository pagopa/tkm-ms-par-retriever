package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard;

import com.fasterxml.jackson.databind.*;
import io.github.resilience4j.circuitbreaker.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.util.*;
import it.gov.pagopa.tkm.ms.parretriever.constant.*;
import okhttp3.mockwebserver.*;
import org.apache.commons.io.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.io.*;
import org.springframework.test.util.*;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class TestMastercardClient {

    @Spy
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
        ReflectionTestUtils.setField(mastercardClient, "isMastercardActive", "true");
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
        try {
            doReturn(buildRequest(testBeans.PAN_1, UUID.randomUUID().toString())).when(mastercardClient).buildRequest(any(), any());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String actualPar = mastercardClient.getPar(testBeans.PAN_1);
        assertEquals(testBeans.PAR_1, actualPar);
    }

    private MastercardParRequest buildRequest(String accountNumber, String requestId) {
        return new MastercardParRequest(requestId,
                new ParRequestEncryptedPayload(new ParRequestEncryptedData(accountNumber, "2022-04-20")));
    }

    @Test
    public void shouldDecorateSupplierAndReturnWithSuccess2() throws Exception {
        System.out.println("\n..........|||| ...... circuitBreaker IS NULL? ");

    }

}
