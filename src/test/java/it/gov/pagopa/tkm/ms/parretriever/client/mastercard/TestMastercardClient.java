package it.gov.pagopa.tkm.ms.parretriever.client.mastercard;

import it.gov.pagopa.tkm.ms.parretriever.constant.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestMastercardClient {

    @InjectMocks
    private MastercardClient mastercardClient;

    private DefaultBeans testBeans;

    @BeforeEach
    void init() {
        testBeans = new DefaultBeans();
    }

    //TODO TESTS

}
