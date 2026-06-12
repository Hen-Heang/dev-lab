package com.learn.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger / OpenAPI configuration.
 *
 * Defines a "bearerAuth" security scheme so the Swagger UI shows an "Authorize"
 * button. Use it to test the protected /api/v1/user and /api/v1/admin endpoints.
 *
 * How to get a token (the login endpoint is handled by JwtAuthenticationFilter,
 * NOT a @Controller, so it does NOT appear in the Swagger list):
 *   1. Register:  POST /api/v1/public/accounts/register
 *   2. Login:     POST /authorization  with { "username": "...", "password": "..." }
 *                 -> copy the "access_token" from the response
 *   3. In Swagger UI click "Authorize" and paste the token (no "Bearer " prefix needed).
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Spring Security JWT Authorization API",
                version = "1.0",
                description = "Demo API secured with stateless JWT. Authorize with a Bearer token to call protected endpoints."
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}
