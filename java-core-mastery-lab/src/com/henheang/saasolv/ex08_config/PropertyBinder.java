package com.henheang.saasolv.ex08_config;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Binds Properties values onto @ConfigProperty-annotated fields via reflection.
 * This is the essence of what Spring Boot does for @ConfigurationProperties
 * (minus relaxed binding, nested objects, type conversion service, etc.).
 */
public class PropertyBinder {

    public static <T> T bind(Class<T> type, Properties props) throws Exception {
        T instance = type.getDeclaredConstructor().newInstance();

        for (Field field : type.getDeclaredFields()) {
            ConfigProperty anno = field.getAnnotation(ConfigProperty.class);
            if (anno == null) continue;

            String raw = props.getProperty(anno.value());
            if (raw == null) continue; // key absent -> leave default

            field.setAccessible(true);
            field.set(instance, convert(field.getType(), raw)); // simple type conversion
        }
        return instance;
    }

    private static Object convert(Class<?> targetType, String raw) {
        if (targetType == int.class || targetType == Integer.class) {
            return Integer.parseInt(raw.trim());
        }
        if (targetType == boolean.class || targetType == Boolean.class) {
            return Boolean.parseBoolean(raw.trim());
        }
        return raw; // String
    }
}
