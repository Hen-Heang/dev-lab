package com.learn.profile;

/**
 * Interface for sending notifications.
 * Different implementations are activated per environment profile.
 */
public interface NotificationService {
    String send(String message);
    String getType();
}
