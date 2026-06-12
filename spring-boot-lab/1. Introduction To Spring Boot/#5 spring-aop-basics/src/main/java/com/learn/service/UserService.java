package com.learn.service;

import com.learn.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Plain service class — NO logging code written here.
 * All logging is handled automatically by LoggingAspect.
 *
 * Notice: this class has ZERO knowledge of AOP.
 * That's the whole point — concerns are separated.
 *
 * @LogExecutionTime on slowSearch() triggers TimingAspect.
 */
@Slf4j
@Service
public class UserService {

    // Fake in-memory data
    private static final Map<Long, String> USERS = Map.of(
            1L, "Hen Heang",
            2L, "Sokha Keo",
            3L, "Dara Pich"
    );

    /**
     * Normal method — LoggingAspect fires @Before and @After automatically.
     */
    public String getUser(Long id) {
        String user = USERS.get(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + id);
        }
        return user;
    }

    /**
     * Normal method — LoggingAspect fires automatically.
     */
    public List<String> getAllUsers() {
        return List.copyOf(USERS.values());
    }

    /**
     * @LogExecutionTime triggers TimingAspect (@Around).
     * Simulates a slow operation so you can see the timing in the log.
     */
    @LogExecutionTime
    public List<String> slowSearch(String keyword) throws InterruptedException {
        // Simulate slow DB query
        Thread.sleep(150);
        return USERS.values().stream()
                .filter(name -> name.toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }

    /**
     * This method throws — watch @AfterThrowing fire in the log.
     */
    public String triggerError() {
        throw new RuntimeException("Something went wrong in UserService!");
    }
}