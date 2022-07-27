package it.gov.pagopa.tkm.ms.parretriever.config;

import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.service.impl.*;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.explore.*;
import org.springframework.batch.core.launch.*;
import org.springframework.batch.core.partition.support.*;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.core.task.*;
import org.springframework.scheduling.annotation.*;

import java.util.*;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobExplorer jobExplorer;

    @Value("${batch-execution.max-number-of-threads}")
    private Integer maxNumberOfThreads;

    @Scheduled(cron = "${batch.par-retriever.cron}")
    public void run() throws Exception {
        jobLauncher.run(parFinderJob(), new JobParametersBuilder().toJobParameters());
    }

    @Bean
    @StepScope
    public ItemReader<ParlessCard> reader(@Value("#{stepExecutionContext['cardList']}") List<ParlessCard> cards) {
        return new CardReader(cards);
    }

    @Bean
    @StepScope
    public ItemProcessor<ParlessCard, ParlessCard> processor() {
        return new CardProcessor();
    }

    @Bean
    @StepScope
    public ItemWriter<ParlessCard> writer(@Value("#{stepExecutionContext['cardList']}") List<ParlessCard> cards,
                                          @Value("#{stepExecutionContext['rateLimit']}") Double rateLimit) {
        return new CardWriter(cards, rateLimit);
    }

    @Bean
    public Job parFinderJob() {
        return jobs.get("parFinderJob").start(parFinderStepManager()).build();
    }

    @Bean
    public Partitioner partitioner() {
        return new CardPartitioner();
    }

    @Bean
    public Step parFinderStepManager() {
        return steps.get("parFinderStep.manager")
                .partitioner("partitionedParFinderStep", partitioner())
                .step(parFinderStep())
                .gridSize(maxNumberOfThreads)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step parFinderStep() {
        return steps.get("parFinderStep")
                .<ParlessCard, ParlessCard>chunk(Integer.MAX_VALUE)
                .reader(reader(null))
                .processor(processor())
                .writer(writer(null, null))
                .allowStartIfComplete(true)
                .build();
    }

}
