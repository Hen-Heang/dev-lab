package com.learn;

import com.learn.model.Role;
import com.learn.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringSecurityJwtAuthorizationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityJwtAuthorizationApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(RoleRepository roleRepository) {
		return args -> {
			Role role =new Role();
			role.setId(0L);
			role.setName("ADMIN");
			roleRepository.save(role);
		};
	}

}
