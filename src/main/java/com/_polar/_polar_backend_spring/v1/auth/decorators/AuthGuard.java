package com._polar._polar_backend_spring.v1.auth.decorators;

import com._polar._polar_backend_spring.v1.auth.enums.ROLES;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthGuard {
    ROLES[] value();
}
