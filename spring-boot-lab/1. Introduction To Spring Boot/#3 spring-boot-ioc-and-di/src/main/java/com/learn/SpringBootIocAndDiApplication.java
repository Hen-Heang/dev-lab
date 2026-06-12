package com.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ====================================================
 * SPRING IoC CONTAINER & DEPENDENCY INJECTION
 * ====================================================
 *
 * IoC (Inversion of Control):
 *   You don't create objects yourself (new MyService()).
 *   The Spring container creates, wires, and manages them for you.
 *
 * DI (Dependency Injection):
 *   Spring "injects" dependencies into your class automatically.
 *
 * @SpringBootApplication = 3 annotations combined:
 *   @Configuration        — this class can define @Bean methods
 *   @ComponentScan        — scan current package + sub-packages for @Component, @Service, etc.
 *   @EnableAutoConfiguration — auto-configure Spring based on classpath
 *
 * Explore the packages in this module:
 *   di/      — injection styles (constructor, field, setter)
 *   scope/   — bean scopes (singleton, prototype)
 *   lifecycle/ — bean lifecycle (@PostConstruct, @PreDestroy)
 *   qualifier/ — selecting between multiple beans of the same type
 */
@SpringBootApplication
public class SpringBootIocAndDiApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringBootIocAndDiApplication.class, args);
	}
}
