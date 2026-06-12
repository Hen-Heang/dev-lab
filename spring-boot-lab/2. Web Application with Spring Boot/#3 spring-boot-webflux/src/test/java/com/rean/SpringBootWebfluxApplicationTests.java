package com.learn;

import com.learn.model.Product;
import com.learn.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

/**
 * WebTestClient — the reactive equivalent of MockMvc / TestRestTemplate.
 * @AutoConfigureWebTestClient — auto-configures WebTestClient for the running app.
 *
 * StepVerifier — used to test reactive streams (Mono/Flux) directly.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class SpringBootWebfluxApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll().block(); // block() only acceptable in tests
    }

    @Test
    void createProduct_shouldReturn201() {
        Product product = Product.builder().name("Laptop").price(new BigDecimal("999.99")).category("Electronics").build();

        webTestClient.post().uri("/api/products")
                .bodyValue(product)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Product.class)
                .value(p -> {
                    assert p.getId() != null;
                    assert p.getName().equals("Laptop");
                });
    }

    @Test
    void getAll_shouldReturnFluxOfProducts() {
        // Test repository directly with StepVerifier
        Product p1 = Product.builder().name("Phone").price(new BigDecimal("499.99")).category("Electronics").build();
        Product p2 = Product.builder().name("Desk").price(new BigDecimal("299.99")).category("Furniture").build();

        StepVerifier.create(
                productRepository.save(p1)
                        .then(productRepository.save(p2))
                        .thenMany(productRepository.findAll())
        )
        .expectNextCount(2)
        .verifyComplete();
    }

    @Test
    void functionalRoute_getAll_shouldWork() {
        webTestClient.get().uri("/api/v2/products")
                .exchange()
                .expectStatus().isOk();
    }
}
