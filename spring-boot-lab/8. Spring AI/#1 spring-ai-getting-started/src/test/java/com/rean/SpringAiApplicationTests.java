package com.learn;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * NOTE: These tests require a valid OPENAI_API_KEY environment variable.
 * For unit testing without a real API key, mock the ChatModel bean.
 *
 * Run with: OPENAI_API_KEY=sk-... mvn test
 */
@SpringBootTest
@TestPropertySource(properties = "spring.ai.openai.api-key=test-key")
class SpringAiApplicationTests {

    @Test
    void contextLoads() {
        // Verifies that the application context starts correctly
    }
}
