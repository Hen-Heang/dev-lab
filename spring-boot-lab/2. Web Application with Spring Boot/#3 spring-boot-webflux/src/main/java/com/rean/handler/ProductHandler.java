package com.learn.handler;

import com.learn.model.Product;
import com.learn.service.impl.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Functional-style handler (alternative to @RestController).
 * Used together with ProductRouter (RouterFunction).
 *
 * Benefits over annotation-based:
 * - Fully explicit routing (no annotation scanning)
 * - Easier to compose and test in isolation
 * - Better visibility of all routes in one place
 */
@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final ProductServiceImpl productService;

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return ServerResponse.ok().body(productService.getAll(), Product.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return productService.getById(id)
                .flatMap(product -> ServerResponse.ok().bodyValue(product))
                .onErrorResume(e -> ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(Product.class)
                .flatMap(productService::create)
                .flatMap(saved -> ServerResponse.status(201).bodyValue(saved));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return request.bodyToMono(Product.class)
                .flatMap(product -> productService.update(id, product))
                .flatMap(updated -> ServerResponse.ok().bodyValue(updated))
                .onErrorResume(e -> ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return productService.delete(id)
                .then(ServerResponse.noContent().build());
    }
}
