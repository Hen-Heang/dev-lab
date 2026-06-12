package com.learn.repository;

import com.learn.model.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 * ReactiveCrudRepository returns Mono/Flux instead of Optional/List.
 * All operations are non-blocking.
 */
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {
    Flux<Product> findByCategory(String category);
}
