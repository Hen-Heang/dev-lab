package com.learn.event;

import org.springframework.context.ApplicationEvent;

/**
 * ====================================================
 * SPRING APPLICATION EVENT
 * ====================================================
 *
 * An event is just a plain object that carries data.
 * Extend ApplicationEvent (or use any POJO from Spring 4.2+).
 *
 * This event is published when a new user registers.
 * Any number of listeners can react to it — independently.
 *
 * Think of it like:
 *   A radio station broadcasts a signal (publishes event).
 *   Many radios can tune in and react (listeners).
 *   The station doesn't know or care how many radios are listening.
 */
public class UserRegisteredEvent extends ApplicationEvent {

    private final Long userId;
    private final String name;
    private final String email;

    public UserRegisteredEvent(Object source, Long userId, String name, String email) {
        super(source);
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public Long getUserId()   { return userId; }
    public String getName()   { return name; }
    public String getEmail()  { return email; }

    @Override
    public String toString() {
        return "UserRegisteredEvent{userId=%d, name='%s', email='%s'}".formatted(userId, name, email);
    }
}
