package com.learn.service.impl;

import com.learn.model.Product;
import com.learn.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl {

    private final ProductRepository productRepository;

    public Mono<Product> create(Product product) {
        return productRepository.save(product);
    }

    public Mono<Product> getById(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found: " + id)));
    }

    public Flux<Product> getAll() {
        return productRepository.findAll();
    }

    public Flux<Product> getByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public Mono<Product> update(Long id, Product product) {
        return getById(id).flatMap(existing -> {
            existing.setName(product.getName());
            existing.setPrice(product.getPrice());
            existing.setCategory(product.getCategory());
            return productRepository.save(existing);
        });
    }

    public Mono<Void> delete(Long id) {
        return productRepository.deleteById(id);
    }
}
