package com.learn;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * A standalone CommandLineRunner bean.
 *
 * @Component tells @ComponentScan to pick this class up and register it as a
 * bean. Because it implements CommandLineRunner, Spring Boot runs its run()
 * method once at startup.
 *
 * This one has NO @Order, so it runs AFTER the ordered ones
 * (@Order defaults to the lowest priority / largest value).
 * Watch the console order: Test2 (@Order 2) vs this one.
 */
@Component
public class CommandLineRunnerTest1 implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Testing CommandLineRunner 1");
    }
}
