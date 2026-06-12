package com.learn.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * ====================================================
 * @ConfigurationProperties vs @Value
 * ====================================================
 *
 * @Value("${app.name}"):
 *   - Good for ONE property at a time
 *   - No type safety (everything is a String first)
 *   - Scattered across many classes — hard to manage
 *
 * @ConfigurationProperties(prefix = "app"):
 *   - Groups related properties into one class
 *   - Full type safety (Spring converts types for you)
 *   - Can be @Validated with Jakarta Bean Validation
 *   - IDE autocomplete when spring-boot-configuration-processor is in pom.xml
 *   - Easy to mock in tests
 *
 * This class binds all "app.*" properties from application.yml.
 */
@Data
@Validated
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    @NotBlank
    private String name;

    @NotBlank
    private String version;

    @Positive
    private int maxUploadSizeMb;

    private Email email = new Email();

    @Data
    public static class Email {
        private String host;
        private int port;
        private String username;
        private boolean ssl;
    }
}
