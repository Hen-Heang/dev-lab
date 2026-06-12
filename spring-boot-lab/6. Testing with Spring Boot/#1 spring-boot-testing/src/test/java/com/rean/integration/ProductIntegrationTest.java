package com.learn.integration;

import com.learn.model.Product;
import com.learn.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @SpringBootTest — loads the FULL application context with all beans.
 * Uses H2 in-memory database (from application.properties).
 * Uses TestRestTemplate to make real HTTP calls to the running server.
 * Slower than slice tests but tests the full stack end-to-end.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void createAndGetProduct_fullStack() {
        Product product = Product.builder().name("Laptop").price(new BigDecimal("999.99")).category("Electronics").build();

        ResponseEntity<Product> createResponse = restTemplate.postForEntity("/api/products", product, Product.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();

        Long id = createResponse.getBody().getId();
        ResponseEntity<Product> getResponse = restTemplate.getForEntity("/api/products/" + id, Product.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getName()).isEqualTo("Laptop");
    }

    @Test
    void getAll_shouldReturnAllProducts() {
        productRepository.save(Product.builder().name("Phone").price(new BigDecimal("499.99")).category("Electronics").build());
        productRepository.save(Product.builder().name("Desk").price(new BigDecimal("299.99")).category("Furniture").build());

        ResponseEntity<Product[]> response = restTemplate.getForEntity("/api/products", Product[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }
}
