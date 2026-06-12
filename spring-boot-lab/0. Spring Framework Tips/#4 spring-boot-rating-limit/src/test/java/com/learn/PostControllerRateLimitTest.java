package com.learn;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Proves the Bucket4j rate limit. With user.type=GOLD the bucket holds 5 tokens,
 * so the first 5 calls succeed (200) and the 6th is rejected (429).
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "user.type=GOLD")
class PostControllerRateLimitTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void allowsUpToTheLimitThenReturns429() throws Exception {
        // First 5 requests are within the GOLD limit.
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(get("/api/posts"))
                    .andExpect(status().isOk());
        }

        // 6th request exhausts the bucket.
        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isTooManyRequests());
    }
}
