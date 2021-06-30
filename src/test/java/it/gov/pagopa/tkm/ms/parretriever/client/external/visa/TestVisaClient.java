package it.gov.pagopa.tkm.ms.parretriever.client.external.visa;

import com.fasterxml.jackson.databind.*;
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
import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestVisaClient {

    @InjectMocks
    private VisaClient visaClient;

    @Mock
    private ObjectMapper mockMapper;

    private DefaultBeans testBeans;

    private final ObjectMapper mapper = new ObjectMapper();

    private static MockWebServer mockServer;

    private static String clientPrivateKey;
    private static String clientPublicCert;

    private static final UUID UUID_TEST = UUID.fromString("c1f77e6e-8fc7-42d2-8128-58ca293e3b42");

    private final MockedStatic<UUID> mockedUuid = mockStatic(UUID.class);
    private final MockedStatic<Instant> mockedInstant = mockStatic(Instant.class);

    @BeforeEach
    void init() {
        testBeans = new DefaultBeans();
        mockedUuid.when(UUID::randomUUID).thenReturn(UUID_TEST);
        mockedInstant.when(Instant::now).thenReturn(DefaultBeans.INSTANT);
        ReflectionTestUtils.setField(visaClient, "publicCert", new ClassPathResource("public_cert_test.p12"));
        ReflectionTestUtils.setField(visaClient, "keystorePassword", "password");
        ReflectionTestUtils.setField(visaClient, "userId", "TEST_USER_ID");
        ReflectionTestUtils.setField(visaClient, "password", "TEST_PASSWORD");
        ReflectionTestUtils.setField(visaClient, "clientPrivateKey", clientPrivateKey);
        ReflectionTestUtils.setField(visaClient, "serverPublicCertificate", clientPublicCert);
        ReflectionTestUtils.setField(visaClient, "keyId", "TEST_KEY_ID");
        ReflectionTestUtils.setField(visaClient, "clientId", "TEST_CLIENT_ID");
        ReflectionTestUtils.setField(visaClient, "retrieveParUrl", "http://localhost:" + mockServer.getPort());
        ReflectionTestUtils.setField(visaClient, "mapper", mapper);
    }

    @BeforeAll
    static void setUp() throws IOException {
        mockServer = new MockWebServer();
        clientPrivateKey = FileUtils.readFileToString(new File("src/test/resources/client_private_key_test.key"));
        clientPublicCert = FileUtils.readFileToString(new File("src/test/resources/client_public_cert_test.crt"));
        mockServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockServer.shutdown();
    }

    @Test
    void givenPan_assertApiGetsCalled() throws Exception {
        String responseAsStringEnc = mapper.writeValueAsString(testBeans.VISA_PAR_ENC_RESPONSE);
        MockResponse mockResponse = new MockResponse().setBody(responseAsStringEnc);
        mockServer.enqueue(mockResponse);
        String actualPar = visaClient.getPar(testBeans.PAN_1);
        assertEquals(testBeans.PAR_1, actualPar);
    }

}
