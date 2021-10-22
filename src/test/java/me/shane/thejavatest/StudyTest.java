package me.shane.thejavatest;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class) // 언더스코어를 공백으로 치환하는 전략을 클래스 내 모든 테스트에 적용
class StudyTest {

//    @Test
//    @DisplayName("스터디 만들기") // 권장
//    void create_new_study() {
//        Study study = new Study(-10);
//
//        // 하나의 assert문이 실패하더라도 assertAll 안에 있는 것은 다 실행되어 테스트 가능
//        assertAll(
//                () -> assertNotNull(study),
//                // 1 param: 기대값, 2 param: 실제값
//                // assertEquals(StudyStatus.DRAFT, study.getStatus(), "스터디를 처음 만들면 상태값이 DRAFT여야 한다."); // 실패시 메시지 추가 가능
//                //문자열 연산 비용이 걱정될 경우 람다 표현식 사용 -> Supplier 사용시 실패할 때만 문자열 연산 실행
//                () -> assertEquals(StudyStatus.DRAFT, study.getStatus(), () -> "스터디를 처음 만들면 상태값이 DRAFT여야 한다."),
//                () -> assertTrue(study.getLimit() > 0)
//        );
//    }

    @DisplayName("변수 테스트")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(strings = {"날씨가", "많이", "추워지고", "있네요"}) // 배열길이 만큼 반복하면서 인자로 전달
    @EmptySource // 비워져 있는 값을 파라미터로 전달
    @NullSource // null을 넣어줌
//    @NullAndEmptySource -> @EmptySource + @NullSource
    void parameterizedTest(String message) {
        System.out.println(message);
    }

    @DisplayName("암묵적 변수 변환 테스트")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(ints = {10, 20 ,30}) // 숫자를 Study 타입으로 변환하고자 함
    void parameterizedTest(@ConvertWith(StudyConverter.class) Study study) { // 하단에서 SimpleArgumentConverter를 상속받은 구현체를 이용하여 int -> Study로 타입 변환
        System.out.println(study.getLimit());
    }

    static class StudyConverter extends SimpleArgumentConverter { // 하나의 인자를 변환해줌
        @Override
        protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
            assertEquals(Study.class, targetType, "Can only convert to Study");
            return new Study(Integer.parseInt(source.toString()));
        }
    }


    @FastTest
    @DisplayName("스터디 만들기") // 권장
    void create_new_study() {
        // 조건별 실행
        // 환경변수 값이 맞아야 하단의 테스트를 실행
        String test_env = System.getenv("TEST_ENV");
        System.out.println(test_env);
        assumeTrue("LOCAL".equalsIgnoreCase(test_env));

        Study actual = new Study(10);
        assertThat(actual.getLimit()).isGreaterThan(0);
    }

    @SlowTest
    @DisplayName("스터디 만들기 🤔")
//    @Disabled // Test 실행 안하게 하는 어노테이션
    void create_new_study_again() {
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