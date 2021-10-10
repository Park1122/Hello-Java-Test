package me.shane.thejavatest;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Test // meta
@Tag("slow") // meta
public @interface SlowTest {
    //meta annotation들을 조합하여 새로운 annotation 생성
}
