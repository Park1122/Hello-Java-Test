package me.shane.thejavatest.junit5;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Test // meta
@Tag("fast") // meta
public @interface FastTest {
    //meta annotation들을 조합하여 새로운 annotation 생성
}
