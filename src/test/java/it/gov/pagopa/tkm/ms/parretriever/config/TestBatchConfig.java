package it.gov.pagopa.tkm.ms.parretriever.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestBatchConfig {

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
