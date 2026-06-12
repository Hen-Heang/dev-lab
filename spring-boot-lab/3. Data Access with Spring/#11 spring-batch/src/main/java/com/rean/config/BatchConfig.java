package com.learn.config;

import com.learn.batch.ProductCsvRow;
import com.learn.batch.ProductItemProcessor;
import com.learn.model.Product;
import com.learn.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Spring Batch 5 configuration.
 *
 * Key differences from Batch 4 (Spring Boot 2.x):
 * - No JobBuilderFactory / StepBuilderFactory (removed)
 * - Inject JobRepository directly and use new JobBuilder(name, jobRepository)
 * - chunk() requires PlatformTransactionManager as second argument
 * - @EnableBatchProcessing is optional (auto-configured by Spring Boot)
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final ProductItemProcessor processor;
    private final ProductRepository productRepository;

    @Bean
    public FlatFileItemReader<ProductCsvRow> csvReader() {
        FlatFileItemReader<ProductCsvRow> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("products.csv"));
        reader.setLinesToSkip(1); // skip header row

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("name", "price", "category");

        BeanWrapperFieldSetMapper<ProductCsvRow> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(ProductCsvRow.class);

        DefaultLineMapper<ProductCsvRow> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean
    public RepositoryItemWriter<Product> jpaWriter() {
        RepositoryItemWriter<Product> writer = new RepositoryItemWriter<>();
        writer.setRepository(productRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step importStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("importProductStep", jobRepository)
                .<ProductCsvRow, Product>chunk(5, transactionManager) // process 5 rows per transaction
                .reader(csvReader())
                .processor(processor)
                .writer(jpaWriter())
                .build();
    }

    @Bean
    public Job importProductJob(JobRepository jobRepository, Step importStep) {
        return new JobBuilder("importProductJob", jobRepository)
                .incrementer(new RunIdIncrementer()) // allows re-running same job
                .start(importStep)
                .build();
    }
}
