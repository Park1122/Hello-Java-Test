package me.shane.thejavatest;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class StudyTest {

    @Test
    void create() {
        Study study = new Study();
        assertNotNull(study);
        System.out.println("create");
    }

    @Test
    @Disabled // Test 실행 안하게 하는 어노테이션
    void create1() {
        System.out.println("create1");
    }

    @BeforeAll // 모든 테스트 실행 전 한번 실행
    static void beforeAll() {
        System.out.println("before all");
    }

    @AfterAll // 모든 테스트 실행 후 한번 실행
    static void afterAll() {
        System.out.println("after all");
    }

    @BeforeEach // 각각의 테스트들이 실행되기 전에 한번씩 실행
    void beforeEach() {
        System.out.println("Before each");
    }

    @AfterEach // 각각의 테스트들이 실행된 후 한번씩 실행
    void afterEach() {
        System.out.println("After each");
    }
}