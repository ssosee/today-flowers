package quokka.todayflowers.domain.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import quokka.AppTest;
import quokka.todayflowers.domain.entity.Member;
import quokka.todayflowers.domain.entity.SocialType;
import quokka.todayflowers.domain.repository.MemberRepository;
import quokka.todayflowers.global.constant.ConstMember;
import quokka.todayflowers.global.exception.ChangePasswordException;
import quokka.todayflowers.global.exception.CommonException;
import quokka.todayflowers.global.exception.JoinException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@AppTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    String userId = "userId";
    String email = "user@kakao.com";

    @Test
    @DisplayName("일반 회원가입")
    void joinTest() {
        //회원 가입
        memberService.join(userId, "321", "321", email, SocialType.NONE);

        // 회원 조회
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        Member member = optionalMember.get();

        assertAll(
                () -> assertEquals(member.getUserId(), userId),
                () -> assertEquals(member.getEmail(), email),
                () -> assertEquals(0, member.getLoginFailCount()),
                () -> assertEquals("USER", member.getRole()),
                () -> assertEquals(SocialType.NONE, member.getSocialType())
        );
    }

    @Test
    @DisplayName("일반 회원가입 중복 아이디")
    void 아이디중복_joinExceptionTest() {
        //회원 가입
        memberService.join(userId, "321", "321", email, SocialType.NONE);

        // 아이디 중복 예외 발생
        JoinException e = assertThrows(JoinException.class,
                () -> memberService.join(userId, "123", "123", "user2@kakao.com", SocialType.NONE));
        assertEquals(e.getMessage(), ConstMember.DUPLICATE_USER_ID);
    }

    @Test
    @DisplayName("일반 회원가입 비밀번호 다른 경우")
    void 비밀번호다름_joinExceptionTest() {
        String password1 = "123";
        String password2 = "321";

        // 비밀번호가 같지 않아 예외 발생
        JoinException e = assertThrows(JoinException.class,
                () -> memberService.join(userId, password1, password2, "user2@kakao.com", SocialType.NONE));
        assertEquals(e.getMessage(), ConstMember.PASSWORD_NOT_SAME);
    }

    @Test
    @DisplayName("회원탈퇴")
    void withdrawalTest() {
        //회원 가입
        memberService.join(userId, "321", "321", email, SocialType.NONE);

        // 회원 삭제
        memberService.withdrawalMember(userId);

        // 회원이 없는 예외가 발생해야함
        CommonException e = assertThrows(CommonException.class, () -> memberService.findMember(userId));
        assertEquals(e.getMessage(), ConstMember.MEMBER_NOT_FOUND);
    }

    @Test
    @DisplayName("이메일로 아이디 찾기")
    void findUserIdTest() {
        //회원 가입
        memberService.join("user1", "321", "321", email, SocialType.NONE);
        memberService.join("user2", "123", "123", email, SocialType.NONE);

        // 동일 이메일로 회원가입 중복 가능
        List<String> userIds = memberService.findUserId(email);

        assertEquals(2, userIds.size());
        assertLinesMatch(Arrays.asList("user1", "user2"), userIds);
    }

    @Test
    @DisplayName("비밀번호 변경")
    void changePasswordTest() {
        String oldPassword = "123";
        String newPassword = "321";
        // 회원가입
        memberService.join(userId, oldPassword, oldPassword, email, SocialType.NONE);

        // 비밀번호 변경
        memberService.changePassword(userId, email,oldPassword, newPassword);

        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        Member member = optionalMember.get();

        // 비밀번호 검증
        assertAll(
                () -> assertEquals(member.getUserId(), userId),
                () -> assertTrue(passwordEncoder.matches(newPassword, member.getPassword()))
        );
    }

    @Test
    @DisplayName("비밀번호 변경")
    void 기존비밀번호다름_changePasswordTest() {
        String oldPassword = "123";
        String newPassword = "321";
        // 회원가입
        memberService.join(userId, oldPassword, oldPassword, email, SocialType.NONE);

        // 비밀번호 변경
        ChangePasswordException e = assertThrows(ChangePasswordException.class,
                () -> memberService.changePassword(userId, email, "789", newPassword));
        assertEquals(e.getMessage(), ConstMember.CHANGE_PASSWORD_FAIL_FOR_OLD_PASSWORD);
    }

    @Test
    @DisplayName("이메일 변경")
    void changeEmailTest() {
        // 회원가입
        memberService.join(userId, "123", "123", email, SocialType.NONE);

        String newEmail = "newUser@kakao.com";
        // 이메일 변경
        memberService.changeEmail(userId, newEmail);

        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        Member member = optionalMember.get();

        // 이메일 확인
        assertAll(
                () -> assertEquals(member.getUserId(), userId),
                () -> assertEquals(member.getEmail(), newEmail)
        );
    }
}