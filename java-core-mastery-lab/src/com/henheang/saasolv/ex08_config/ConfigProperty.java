package com.henheang.saasolv.ex08_config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field that should be bound from a properties key.
 * A hand-rolled mini version of how @ConfigurationProperties maps
 * application.yml keys onto a typed config object in saas-olv (GlobalsProperties).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigProperty {
    String value(); // the property key, e.g. "globals.file.storage-type"
}
