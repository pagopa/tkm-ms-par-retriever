package it.gov.pagopa.tkm.ms.parretriever.client.external.amex;

import com.fasterxml.jackson.databind.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.amex.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.constant.*;
import okhttp3.mockwebserver.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.springframework.test.util.*;

import java.io.*;
import java.lang.reflect.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestAmexClient {

    @InjectMocks
    private AmexClient amexClient;

    @Mock
    private ObjectMapper mockMapper;

    private final ObjectMapper mapper = new ObjectMapper();

    private DefaultBeans testBeans;

    private static MockWebServer mockServer;

    @BeforeEach
    void init() throws Exception {
        testBeans = new DefaultBeans();
        AmexClient.class.getDeclaredField("clientId").setAccessible(true);
        AmexClient.class.getDeclaredField("clientSecret").setAccessible(true);
        AmexClient.class.getDeclaredField("retrieveParUrl").setAccessible(true);
        ReflectionTestUtils.setField(amexClient, "clientId", "TEST_ID");
        ReflectionTestUtils.setField(amexClient, "clientSecret", "TEST_SECRET");
        ReflectionTestUtils.setField(amexClient, "retrieveParUrl", "http://localhost:" + mockServer.getPort());
        Method postConstruct = AmexClient.class.getDeclaredMethod("init");
        postConstruct.setAccessible(true);
        postConstruct.invoke(amexClient);
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
    void givenPan_assertApiGetsCalled() throws IOException {
        String responseAsString = mapper.writeValueAsString(testBeans.AMEX_PAR_RESPONSE);
        MockResponse mockResponse = new MockResponse().setBody(responseAsString);
        mockServer.enqueue(mockResponse);
        when(mockMapper.readValue(responseAsString, AmexParResponse[].class)).thenReturn(mapper.readValue(responseAsString, AmexParResponse[].class));
        String actualPar = amexClient.getPar(testBeans.PAN);
        assertEquals(testBeans.PAR, actualPar);
    }

}
