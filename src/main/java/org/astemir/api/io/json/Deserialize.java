package org.astemir.api.io.json;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ /* No targets allowed */ })
@Retention(RetentionPolicy.RUNTIME)
public @interface Deserialize{
    String value() default "";
}