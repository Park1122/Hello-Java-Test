package me.shane.thejavatest.study;

import me.shane.thejavatest.domain.Member;
import me.shane.thejavatest.domain.Study;
import me.shane.thejavatest.member.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    @Mock
    MemberService memberService;
    @Mock
    StudyRepository studyRepository;

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
        StudyService studyService = new StudyService(memberService, studyRepository);
        Member member = new Member();
        member.setId(1L);
        Study study = new Study(10, "Test");

        // mock stubbing
        when(memberService.findById(1L)).thenReturn(Optional.of(member));
        when(studyRepository.save(study)).thenReturn(study);

        studyService.createNewStudy(1L, study);
        assertEquals(member.getId(), study.getOwnerId());
    }

}