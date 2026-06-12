package com.learn.container;

import com.learn.model.Product;
import com.learn.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testcontainers + @ServiceConnection (Spring Boot 3.1+).
 *
 * @Testcontainers — JUnit 5 extension that manages container lifecycle.
 * @Container — starts a real PostgreSQL Docker container before tests run.
 * @ServiceConnection — Spring Boot automatically reads the container's
 *   host/port/credentials and overrides spring.datasource.* — no manual
 *   @DynamicPropertySource needed.
 *
 * REQUIRES: Docker running locally.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ProductContainerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private ProductRepository productRepository;

    @Test
    void save_shouldPersistToRealPostgres() {
        Product product = Product.builder()
                .name("Postgres Product")
                .price(new BigDecimal("123.45"))
                .category("Test")
                .build();

        Product saved = productRepository.save(product);

        assertThat(saved.getId()).isNotNull();
        assertThat(productRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void containerIsRunning() {
        assertThat(postgres.isRunning()).isTrue();
    }
}
