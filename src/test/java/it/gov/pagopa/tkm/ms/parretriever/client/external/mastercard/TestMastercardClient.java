package it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard;

import com.fasterxml.jackson.databind.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.util.*;
import it.gov.pagopa.tkm.ms.parretriever.constant.*;
import okhttp3.mockwebserver.*;
import org.apache.commons.io.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.springframework.core.io.*;
import org.springframework.test.util.*;

import java.io.*;
import java.lang.reflect.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestMastercardClient {

    @InjectMocks
    private MastercardClient mastercardClient;

    private static MockWebServer mockServer;

    private DefaultBeans testBeans;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void init() throws Exception {
        testBeans = new DefaultBeans();
        MastercardClient.class.getDeclaredField("privateDecryptionKey").setAccessible(true);
        MastercardClient.class.getDeclaredField("publicEncryptionKey").setAccessible(true);
        MastercardClient.class.getDeclaredField("signingKeyCert").setAccessible(true);
        MastercardClient.class.getDeclaredField("signingKeyPassword").setAccessible(true);
        MastercardClient.class.getDeclaredField("consumerKey").setAccessible(true);
        MastercardClient.class.getDeclaredField("retrieveParUrl").setAccessible(true);
        MastercardClient.class.getDeclaredField("api").setAccessible(true);
        ReflectionTestUtils.setField(mastercardClient, "privateDecryptionKey", FileUtils.readFileToString(new File("src/test/resources/client_private_key_test.key")));
        ReflectionTestUtils.setField(mastercardClient, "publicEncryptionKey", new ClassPathResource("client_public_cert_test.crt"));
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
    void givenPan_assertApiGetsCalled() throws Exception {
        MockResponse mockResponse = new MockResponse().setBody(mapper.writeValueAsString(testBeans.MASTERCARD_PAR_RESPONSE));
        mockServer.enqueue(mockResponse);
        String actualPar = mastercardClient.getPar(testBeans.PAN);
        assertEquals(testBeans.PAR, actualPar);
    }

}
