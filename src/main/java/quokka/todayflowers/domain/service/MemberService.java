package quokka.todayflowers.domain.service;

import jakarta.mail.MessagingException;
import quokka.todayflowers.domain.entity.Member;
import quokka.todayflowers.domain.entity.SocialType;
import quokka.todayflowers.web.response.MyPageForm;

import java.util.List;
import java.util.concurrent.Future;

public interface MemberService {
    // 회원가입
    void join(String userId, String password1, String password2, String email, SocialType socialType);
    // 로그인
    // 스프링 시큐리티 FormLogin, OAuth2Login으로 불필요함
    @Deprecated Boolean login(String userId, String password);
    // 회원탈퇴
    void withdrawalMember(String userId);
    // 아이디 찾기
    List<String> findUserId(String email);
    // 회원 찾기
    MyPageForm findMember(String userId);
    // 방문횟수 증가
    void hitsUp(String userId);
    // 메일 전송(임시 비밀번호 생성)
    void sendMailForCreateTemporaryPassword(String userId, String FromEmail, String toEmail) throws MessagingException;
    // 비밀번호 변경
    void changePassword(String userId, String email, String oldPassword, String newPassword);
    // 회원 검증
    Member validationMemberByUserIdAndEmail(String userId, String toEmail);
    // 이메일 변경
    void changeEmail(String userId, String email);
}
