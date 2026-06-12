package com.learn;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Another CommandLineRunner, this time WITH an explicit order.
 *
 * @Order(2) means it runs after the main app's runner (@Order 1) but before
 * the un-ordered CommandLineRunnerTest1. Run the app and read the console
 * output top-to-bottom to see ordering in action.
 */
@Order(value = 2)
@Component
public class CommandLineRunnerTest2 implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Testing CommandLineRunner 2");
    }
}
