package com.learn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;

/**
 * A "first look under the hood" of Spring Boot startup.
 *
 * This class is both the application entry point AND a CommandLineRunner.
 * A CommandLineRunner is a bean whose run(...) method Spring Boot calls ONCE,
 * automatically, right AFTER the application context is fully started - a
 * handy place for startup tasks (seeding data, sanity checks, demos).
 *
 * @Order(1) controls the ORDER when several CommandLineRunners exist:
 * lower number = runs earlier. (See CommandLineRunnerTest1/2 in this project.)
 */
@Order(value = 1)
@SpringBootApplication
public class SpringBoot3FirstViewApplication implements CommandLineRunner {

	public static void main(String[] args) {
		//	SpringApplication.run(SpringBoot3FirstViewApplication.class, args);

		// Here we DON'T use the one-line run() shortcut, so we can customize
		// the app before starting it - in this case turning off the startup
		// ASCII "Spring" banner in the console.
		SpringApplication app = new SpringApplication(SpringBoot3FirstViewApplication.class);
		app.setBannerMode(Banner.Mode.OFF);   // try Mode.CONSOLE to see the banner
		app.run(args);
	}

	// @Autowired = "Spring, inject the bean of this type here" (field injection).
	// The ApplicationContext IS the IoC container - it holds every bean and you
	// can ask it for them by name or type.
	@Autowired
	ApplicationContext applicationContext;

	// Called automatically after startup because this class implements CommandLineRunner.
	@Override
	public void run(String... args) throws Exception {
		System.out.println("Testing CommandLine Runner");

		// Uncomment to print every bean Spring created - a great way to SEE
		// how much auto-configuration registers for you behind the scenes.
		/* String[] beans = applicationContext.getBeanDefinitionNames();
		Arrays.sort(beans);
		for(String b : beans) {
			System.out.println("Bean name " + b + " => " + applicationContext.getBean(b).getClass());
		}*/
	}
}
