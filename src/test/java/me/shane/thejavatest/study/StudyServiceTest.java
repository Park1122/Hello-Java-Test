package me.shane.thejavatest.study;

import lombok.extern.slf4j.Slf4j;
import me.shane.thejavatest.domain.Member;
import me.shane.thejavatest.domain.Study;
import me.shane.thejavatest.member.MemberService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@Slf4j
@Testcontainers // 하단의 메뉴얼하게 testcontainers를 start, stop한 것을 알아서 해줌
@ContextConfiguration(initializers = StudyServiceTest.ContainerPropertyInitializer.class)
class StudyServiceTest {

    @Mock
    MemberService memberService;

    @Mock
    StudyRepository studyRepository;

//    @Autowired
//    Environment environment;

    @Value("${container.port}") int port;

    @Container // docker testcontainers를 사용하려면 docker가 작동중이어야 함.
    static GenericContainer postgreSQLContainer = new GenericContainer("postgres")
            .withExposedPorts(5432)
            .withEnv("POSTGRES_DB", "studytest") // 사용하지 않는 호스트의 포트가 랜덤하게 선택되어 매핑됨
            .withEnv("POSTGRES_HOST_AUTH_METHOD", "trust");
    @BeforeAll
    static void beforeAll() {
        Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(log);
        postgreSQLContainer.followOutput(logConsumer); // streaming으로 log 확인
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("==================");
//        System.out.println(environment.getProperty("container.port")); // 매핑된 호스트포트를 출력
        System.out.println(port); // 매핑된 호스트포트를 출력
        System.out.println(postgreSQLContainer.getLogs()); // 지금까지의 log 몰아서 보기
        studyRepository.deleteAll();
    }

    @Test
    void createStudyService() {
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("cco2416@email.com");

        // stubbing -> mock 객체가 해야할 행동을 정의하는 것
        // when 일 때, thenReturn 의 값을 반환하라고 정의하는 것임.
//        when(memberService.findById(1L)).thenReturn(Optional.of(member));
        // ArgumentMatchers.any 사용시, 항상 일정하게 반환값 나오게 설정 가능
        when(memberService.findById(any())).thenReturn(Optional.of(member));

        Study study = new Study(10, "java");

        assertEquals("cco2416@email.com",  memberService.findById(1L).get().getEmail());
        assertEquals("cco2416@email.com",  memberService.findById(2L).get().getEmail());

        // validate 메소드가 1의 매개변수로 받아서 실행될 경우 exception 반환
        doThrow(new IllegalArgumentException()).when(memberService).validate(1L);

        assertThrows(IllegalArgumentException.class, () -> {
            memberService.validate(1L);
        });

    }

    @Test
    void practice() {
        // Given
        StudyService studyService = new StudyService(memberService, studyRepository);
        Member member = new Member();
        member.setId(1L);
        Study study = new Study(10, "Test");

        // mock stubbing
        when(memberService.findById(1L)).thenReturn(Optional.of(member));
        when(studyRepository.save(study)).thenReturn(study);

        // When
        studyService.createNewStudy(1L, study);

        // Then
        assertEquals(member.getId(), study.getOwnerId());
    }

    @Test
    void practice_BDD_style() {
        // Given
        StudyService studyService = new StudyService(memberService, studyRepository);
        Member member = new Member();
        member.setId(1L);
        Study study = new Study(10, "Test");

        given(memberService.findById(1L)).willReturn(Optional.of(member));
        given(studyRepository.save(study)).willReturn(study);

        // When
        studyService.createNewStudy(1L, study);

        // Then
        assertEquals(member.getId(), study.getOwnerId());
    }

    static class ContainerPropertyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext context) {
            TestPropertyValues.of("container.port=" + postgreSQLContainer.getMappedPort(5432))
                    .applyTo(context.getEnvironment());
        }
    }
}