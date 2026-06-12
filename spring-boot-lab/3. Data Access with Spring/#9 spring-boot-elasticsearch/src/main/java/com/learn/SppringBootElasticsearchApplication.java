package com.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "com.learn.repository")
public class SppringBootElasticsearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SppringBootElasticsearchApplication.class, args);
	}

}
