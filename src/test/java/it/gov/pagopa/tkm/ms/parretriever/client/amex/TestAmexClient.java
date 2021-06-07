package it.gov.pagopa.tkm.ms.parretriever.client.amex;

import com.fasterxml.jackson.databind.*;
import it.gov.pagopa.tkm.ms.parretriever.constant.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestAmexClient {

    @InjectMocks
    private AmexClient amexClient;

    private DefaultBeans testBeans;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void init() {
        testBeans = new DefaultBeans();
    }

    //TODO TESTS

}
