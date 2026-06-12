package com.learn.repository;

import com.learn.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @DataJpaTest — loads ONLY the JPA layer (entities, repositories).
 * Configures an in-memory H2 database automatically.
 * Does NOT load @Service or @Controller beans.
 * Transactions are rolled back after each test.
 */
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;  // use to set up test data without going through the repo

    @Autowired
    private ProductRepository productRepository;

    @Test
    void save_shouldPersistProduct() {
        Product product = Product.builder().name("Keyboard").price(new BigDecimal("79.99")).category("Electronics").build();
        Product saved = productRepository.save(product);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Keyboard");
    }

    @Test
    void findById_shouldReturnProduct() {
        Product product = entityManager.persistAndFlush(
                Product.builder().name("Mouse").price(new BigDecimal("29.99")).category("Electronics").build()
        );

        Optional<Product> found = productRepository.findById(product.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Mouse");
    }

    @Test
    void findByCategory_shouldReturnMatchingProducts() {
        entityManager.persist(Product.builder().name("Laptop").price(new BigDecimal("999.99")).category("Electronics").build());
        entityManager.persist(Product.builder().name("Desk").price(new BigDecimal("299.99")).category("Furniture").build());
        entityManager.persist(Product.builder().name("Monitor").price(new BigDecimal("399.99")).category("Electronics").build());
        entityManager.flush();

        List<Product> electronics = productRepository.findByCategory("Electronics");

        assertThat(electronics).hasSize(2);
        assertThat(electronics).extracting(Product::getName).containsExactlyInAnyOrder("Laptop", "Monitor");
    }

    @Test
    void delete_shouldRemoveProduct() {
        Product product = entityManager.persistAndFlush(
                Product.builder().name("Tablet").price(new BigDecimal("599.99")).category("Electronics").build()
        );

        productRepository.deleteById(product.getId());

        assertThat(productRepository.findById(product.getId())).isEmpty();
    }
}
