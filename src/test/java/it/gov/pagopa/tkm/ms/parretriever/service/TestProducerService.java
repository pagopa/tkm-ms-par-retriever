package it.gov.pagopa.tkm.ms.parretriever.service;

import it.gov.pagopa.tkm.ms.parretriever.service.impl.ProducerServiceImpl;
import it.gov.pagopa.tkm.service.*;
import org.bouncycastle.openpgp.PGPException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestProducerService {

    @InjectMocks
    private ProducerServiceImpl producerService;

    private final MockedStatic<PgpStaticUtils> pgpStaticUtilsMockedStatic = mockStatic(PgpStaticUtils.class);

    @BeforeEach
    void init() {
        pgpStaticUtilsMockedStatic.when(()-> PgpStaticUtils.encrypt(anyString(), anyString())).thenReturn("encryptedString");
        ReflectionTestUtils.setField(producerService, "pgpPrivateKey", "TEST_PRIVATE_KEY");
        ReflectionTestUtils.setField(producerService, "pgpPassphrase", "TEST_PASSPHRASE");
    }

    @AfterAll
    void close() {
        pgpStaticUtilsMockedStatic.close();
    }

}

