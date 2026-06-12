package com.learn.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * ====================================================
 * BEAN SCOPES
 * ====================================================
 *
 * SINGLETON (default):
 *   Spring creates ONE instance of the bean for the entire application.
 *   Every time you inject or request this bean, you get the SAME object.
 *   Use for: stateless services, repositories, controllers.
 *
 * PROTOTYPE:
 *   Spring creates a NEW instance every time the bean is requested.
 *   Use for: stateful objects, tasks that hold per-request state.
 *
 * WEB SCOPES (only in web apps):
 *   @Scope("request")  — new instance per HTTP request
 *   @Scope("session")  — new instance per user HTTP session
 *
 * ====================================================
 * BEAN LIFECYCLE
 * ====================================================
 *
 * @PostConstruct — called AFTER the bean is created and all dependencies injected.
 *   Use for: initialization logic, opening connections, loading data.
 *
 * @PreDestroy — called BEFORE the bean is destroyed (app shutdown).
 *   Use for: cleanup, closing connections, releasing resources.
 *   NOTE: NOT called for @Scope("prototype") beans (Spring doesn't manage their destruction).
 */
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) // new instance each time
@Getter
public class ScopeDemo {

    private final String instanceId;

    public ScopeDemo() {
        // Each new instance gets a unique ID so we can see they are different objects
        this.instanceId = "instance-" + System.nanoTime();
    }

    @PostConstruct
    public void init() {
        log.info("@PostConstruct — ScopeDemo created: {}", instanceId);
    }

    @PreDestroy
    public void destroy() {
        // For PROTOTYPE scope, this is NOT called automatically.
        // For SINGLETON scope, this is called on application shutdown.
        log.info("@PreDestroy — ScopeDemo destroyed: {}", instanceId);
    }
}
