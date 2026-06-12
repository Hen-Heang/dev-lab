package com.learn;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

/**
 * Customizing the embedded web server PROGRAMMATICALLY.
 *
 * Spring Boot ships an embedded Tomcat. Normally you'd set the port in
 * application.yml (server.port=8282). This class shows the code-based
 * alternative: implement WebServerFactoryCustomizer and Spring Boot will
 * call customize() while building the server.
 *
 * Result: the app starts on http://localhost:8282 instead of the default 8080.
 * (If both this and server.port are set, this customizer wins.)
 */
@Component
public class CustomerApplicationServerPort implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        factory.setPort(8282);
    }
}
