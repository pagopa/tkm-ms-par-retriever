package it.gov.pagopa.tkm.ms.parretriever.config;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class TestBatchConfig {

    @InjectMocks
    public BatchConfig batchConfig;

    @Test
    void reader_itemReader() {
        assertNotNull(batchConfig.reader(new ArrayList<>()));
    }

    @Test
    void processor_itemProcessor() {
        assertNotNull(batchConfig.processor());
    }

    @Test
    void writer_itemWriter() {
        assertNotNull(batchConfig.writer(new ArrayList<>(), 0.0));
    }

    @Test
    void parFinder_getJob() {
        assertThrows(NullPointerException.class, () -> batchConfig.parFinderJob());
    }

    @Test
    void partitioner_getPartitioner() {
        assertNotNull(batchConfig.partitioner());
    }

}
