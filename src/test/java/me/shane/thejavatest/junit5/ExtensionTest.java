package me.shane.thejavatest.junit5;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FindSlowTestExtension.class)
class ExtensionTest {

    @SlowTest
    void slowTest() {
        System.out.println("slow test");
    }

    @Test
    void test() throws InterruptedException {
        Thread.sleep(1005L);
        System.out.println("test");
    }
}
