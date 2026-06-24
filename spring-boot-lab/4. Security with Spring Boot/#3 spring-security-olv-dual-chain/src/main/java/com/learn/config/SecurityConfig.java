package com.learn.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Mirrors saas-olv: egovframework.com.cmm.config.SecurityConfig
 *
 * TWO SecurityFilterChain beans. Only ONE is created, selected by the property
 * `globals.security.mode` via @ConditionalOnProperty:
 *
 *   globals.security.mode = web  (default) -> form login + session
 *   globals.security.mode = api            -> stateless, JSON 401/403
 *
 * This is the headline pattern to learn here: config-driven security policy.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    private final GlobalsProperties globals;

    public SecurityConfig(GlobalsProperties globals) {
        this.globals = globals;
    }

    // =========================================================================
    // [1] WEB mode — form login + session (admin / user web)
    // =========================================================================
    @Bean
    @ConditionalOnProperty(name = "globals.security.mode", havingValue = "web", matchIfMissing = true)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("Security: WEB mode (form login + session)");

        String[] publicUrls = publicUrls();

        http
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/", "/public/**", "/css/**", "/js/**").permitAll();
                if (publicUrls.length > 0) auth.requestMatchers(publicUrls).permitAll();
                auth.anyRequest().authenticated();
            })
            .formLogin(Customizer.withDefaults())   // default /login page
            .logout(Customizer.withDefaults());

        return http.build();
    }

    // =========================================================================
    // [2] API mode — stateless, JSON responses (REST API server)
    // =========================================================================
    @Bean
    @ConditionalOnProperty(name = "globals.security.mode", havingValue = "api")
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("Security: API mode (stateless + JSON 401/403)");

        String[] publicUrls = publicUrls();

        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/api/auth/**", "/api/public/**").permitAll();
                if (publicUrls.length > 0) auth.requestMatchers(publicUrls).permitAll();
                auth.anyRequest().authenticated();
            })
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .httpBasic(Customizer.withDefaults())   // real saas-olv adds a JWT filter here
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, e) -> writeJson(res, 401,
                        "{\"success\":false,\"code\":\"UNAUTHORIZED\",\"message\":\"인증이 필요합니다.\"}"))
                .accessDeniedHandler((req, res, e) -> writeJson(res, 403,
                        "{\"success\":false,\"code\":\"FORBIDDEN\",\"message\":\"접근 권한이 없습니다.\"}"))
            );

        return http.build();
    }

    // =========================================================================
    // Shared beans (used by whichever chain is active)
    // =========================================================================
    @Bean
    public UserDetailsService users(PasswordEncoder encoder) {
        return new InMemoryUserDetailsManager(
            User.withUsername("user").password(encoder.encode("user123")).roles("USER").build(),
            User.withUsername("admin").password(encoder.encode("admin123")).roles("ADMIN").build()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // =========================================================================
    // helpers
    // =========================================================================
    /** Business-level allowlist from application.yml (globals.security.public-urls). */
    private String[] publicUrls() {
        List<String> urls = globals.getSecurity().getPublicUrls();
        if (urls == null || urls.isEmpty()) {
            return new String[0];
        }
        log.info("Security: registered {} public url(s): {}", urls.size(), urls);
        return urls.toArray(new String[0]);
    }

    private void writeJson(HttpServletResponse res, int status, String body) throws java.io.IOException {
        res.setStatus(status);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write(body);
    }
}
