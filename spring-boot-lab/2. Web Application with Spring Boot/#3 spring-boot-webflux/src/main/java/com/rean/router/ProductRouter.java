package com.learn.router;

import com.learn.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

/**
 * Functional routing — all routes defined explicitly in one place.
 * These routes are on /api/v2/products to avoid conflict with the
 * annotation-based controller on /api/products.
 */
@Configuration
public class ProductRouter {

    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductHandler handler) {
        return RouterFunctions.route()
                .GET("/api/v2/products", handler::getAll)
                .GET("/api/v2/products/{id}", handler::getById)
                .POST("/api/v2/products", handler::create)
                .PUT("/api/v2/products/{id}", handler::update)
                .DELETE("/api/v2/products/{id}", handler::delete)
                .build();
    }
}
