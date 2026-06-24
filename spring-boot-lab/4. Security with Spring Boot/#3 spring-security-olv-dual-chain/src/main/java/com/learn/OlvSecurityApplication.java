package com.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.learn.config.GlobalsProperties;

/**
 * saas-olv practice — Spring Security 6 dual SecurityFilterChain.
 *
 * Mirrors egovframework.com.cmm.config.SecurityConfig: ONE app, but the active
 * security policy is chosen at runtime by the property `globals.security.mode`
 * (web = form/session, api = stateless). See {@link com.learn.config.SecurityConfig}.
 */
@SpringBootApplication
@EnableConfigurationProperties(GlobalsProperties.class)
public class OlvSecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(OlvSecurityApplication.class, args);
    }
}
