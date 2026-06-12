package com.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * @ConfigurationPropertiesScan — tells Spring to find all @ConfigurationProperties
 * classes in this package automatically (alternative to @EnableConfigurationProperties).
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class SpringBootConfigAndProfilesApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringBootConfigAndProfilesApplication.class, args);
	}
}
