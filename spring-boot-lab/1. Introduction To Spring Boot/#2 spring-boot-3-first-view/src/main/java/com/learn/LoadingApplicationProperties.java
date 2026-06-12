package com.learn;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Reading values from application.yml/properties with @Value.
 *
 * @Value("${key}") injects a single configuration value into a field.
 * Syntax notes:
 *   ${my.app:DefaultAppName}  -> read property "my.app"; if it's MISSING,
 *                                fall back to "DefaultAppName" (the part after ':').
 *   ${my.list}                -> no default; the app fails to start if it's absent.
 *
 * (For binding many related properties at once, prefer
 *  @ConfigurationProperties - see project #4 config-and-profiles.)
 */
@Component
public class LoadingApplicationProperties {

    // Injected with a safe default, so the app runs even if "my.app" isn't set.
    @Value("${my.app:DefaultAppName}")
    public String myApp;

    // Required property - must exist in application.yml or startup fails.
    @Value("${my.list}")
    private String myList;

    // NOTE: @Bean on a void method is unusual - it's used here just to make
    // Spring call this method once during startup so we can print the values.
    // (A cleaner approach would be @PostConstruct or a CommandLineRunner.)
    @Bean
    public void getMyAppValueFromApplication() {
        System.out.println(myApp);
        if(myList.contains("act")) {
            System.out.println(myList);
        }
    }
}
