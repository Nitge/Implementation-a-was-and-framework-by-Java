package main.java.was.framework.annotation;

import main.java.was.http.header.HttpMethod;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Mapping {
    String value();
    HttpMethod method() default HttpMethod.GET;
}
