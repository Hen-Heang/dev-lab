package com.learn;

import com.learn.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.batch.core.BatchStatus.COMPLETED;

/**
 * @SpringBatchTest — provides JobLauncherTestUtils and other batch test utilities.
 * @SpringBootTest — loads full context including batch configuration.
 */
@SpringBatchTest
@SpringBootTest
class SpringBatchApplicationTests {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private Job importProductJob;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void importProductJob_shouldCompleteAndSaveAllRows() throws Exception {
        jobLauncherTestUtils.setJob(importProductJob);

        JobParameters params = new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis())
                .toJobParameters();

        JobExecution execution = jobLauncherTestUtils.launchJob(params);

        assertThat(execution.getStatus()).isEqualTo(COMPLETED);
        assertThat(productRepository.findAll()).hasSize(10); // 10 rows in products.csv
    }
}
