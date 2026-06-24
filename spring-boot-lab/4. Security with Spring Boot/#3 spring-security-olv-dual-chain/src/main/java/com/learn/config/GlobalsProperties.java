package com.learn.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Mirrors saas-olv: egovframework.com.cmm.config.GlobalsProperties
 *
 * Type-safe binding of `globals.*` from application.yml. Spring Boot maps the
 * YAML tree onto these nested objects (relaxed binding: security.public-urls
 * -> publicUrls).
 */
@ConfigurationProperties(prefix = "globals")
public class GlobalsProperties {

    private Security security = new Security();

    public Security getSecurity() { return security; }
    public void setSecurity(Security security) { this.security = security; }

    public static class Security {
        /** web (form/session) or api (stateless). Default web. */
        private String mode = "web";
        /** URLs open to everyone (business-level allowlist). */
        private List<String> publicUrls = new ArrayList<>();

        public String getMode() { return mode; }
        public void setMode(String mode) { this.mode = mode; }

        public List<String> getPublicUrls() { return publicUrls; }
        public void setPublicUrls(List<String> publicUrls) { this.publicUrls = publicUrls; }
    }
}
