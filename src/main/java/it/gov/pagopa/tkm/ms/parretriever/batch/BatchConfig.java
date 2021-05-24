package it.gov.pagopa.tkm.ms.parretriever.batch;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

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

    @Scheduled(cron = /*TODO da file application*/"0 * * * * *")
    public void run() throws Exception {
        jobLauncher.run(parFinderJob(), new JobParametersBuilder().toJobParameters());
    }

    @Bean
    @StepScope
    public ItemReader</*TODO Card*/Object> reader() {
        return new CardReader();
    }

    @Bean
    @StepScope
    public ItemProcessor</*TODO Card*/Object, String> processor() {
        return new CardProcessor();
    }

    @Bean
    @StepScope
    public ItemWriter<String> writer() {
        return new CardWriter();
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
                .gridSize(/*TODO da file application*/10)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step parFinderStep() {
        return steps.get("parFinderStep")
                .</*TODO Card*/Object, String>chunk(Integer.MAX_VALUE)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .allowStartIfComplete(true)
                .build();
    }

}
