package com.henheang.saasolv.ex07_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mirrors saas-olv: egovframework.com.lnk.cmm.annotation.Token
 *
 * A marker annotation read at RUNTIME by reflection (see {@link RoleChecker}).
 *   - @Retention(RUNTIME) : must survive into the running program to be read
 *   - @Target(METHOD)     : only valid on methods
 *   - String value()      : an attribute, with a default
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequiresRole {
    String value() default "USER";
}
