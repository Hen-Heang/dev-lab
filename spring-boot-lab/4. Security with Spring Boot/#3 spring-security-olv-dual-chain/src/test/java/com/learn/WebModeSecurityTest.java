package com.learn;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Default property is globals.security.mode=web, so the WEB chain is active.
 */
@SpringBootTest
@AutoConfigureMockMvc
class WebModeSecurityTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void publicEndpoint_isOpen() throws Exception {
        mvc.perform(get("/public/ping"))
           .andExpect(status().isOk());
    }

    @Test
    void secureEndpoint_anonymous_redirectsToLogin() throws Exception {
        mvc.perform(get("/secure/me"))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void secureEndpoint_authenticated_isOk() throws Exception {
        mvc.perform(get("/secure/me").with(user("user").roles("USER")))
           .andExpect(status().isOk());
    }
}
