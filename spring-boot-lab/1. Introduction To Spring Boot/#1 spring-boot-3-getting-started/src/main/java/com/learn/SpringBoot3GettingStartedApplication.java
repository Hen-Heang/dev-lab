package com.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * THE ENTRY POINT of every Spring Boot application.
 *
 * @SpringBootApplication is a shortcut that bundles THREE annotations:
 *   1. @Configuration      - this class can define beans (@Bean methods).
 *   2. @EnableAutoConfiguration - Spring Boot looks at the jars on your
 *                             classpath and auto-configures sensible defaults
 *                             (e.g. it sees spring-web -> starts an embedded
 *                             Tomcat server; it sees a JPA + H2 -> sets up a
 *                             DataSource for you). This is the "magic".
 *   3. @ComponentScan      - scans THIS package (com.learn) and all
 *                             sub-packages for your @Component/@Service/
 *                             @Controller/@Repository classes and registers
 *                             them as beans in the IoC container.
 */
@SpringBootApplication
public class SpringBoot3GettingStartedApplication {

	// Plain old Java main method - this is where the JVM starts.
	public static void main(String[] args) {
		// SpringApplication.run(...) does the heavy lifting:
		//  - creates the Spring "ApplicationContext" (the IoC container),
		//  - runs auto-configuration,
		//  - scans for and creates all your beans,
		//  - starts the embedded web server.
		// The first argument tells Spring which class is the configuration root.
		SpringApplication.run(SpringBoot3GettingStartedApplication.class, args);
	}

}
