package com.learn.controller;

import com.learn.model.Product;
import com.learn.service.impl.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Annotation-based WebFlux controller.
 * Looks like a regular Spring MVC controller but returns Mono/Flux.
 * Non-blocking: the thread is released while waiting for I/O.
 *
 * Compare with the functional router/handler in ProductRouter + ProductHandler.
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> create(@RequestBody Product product) {
        return productService.create(product);
    }

    @GetMapping("/{id}")
    public Mono<Product> getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @GetMapping
    public Flux<Product> getAll() {
        return productService.getAll();
    }

    @GetMapping("/category/{category}")
    public Flux<Product> getByCategory(@PathVariable String category) {
        return productService.getByCategory(category);
    }

    @PutMapping("/{id}")
    public Mono<Product> update(@PathVariable Long id, @RequestBody Product product) {
        return productService.update(id, product);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return productService.delete(id);
    }
}
