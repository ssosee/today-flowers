package quokka.todayflowers.domain.service;

import jakarta.mail.MessagingException;
import quokka.todayflowers.domain.entity.Member;
import quokka.todayflowers.domain.entity.SocialType;
import quokka.todayflowers.web.response.MyPageForm;

import java.util.List;
import java.util.concurrent.Future;

public interface MemberService {
    // 회원가입
    Boolean join(String userId, String password, String email, SocialType socialType);
    // 로그인
    Boolean login(String userId, String password);
    // 회원탈퇴
    Boolean withdrawalMember(String userId);
    // 아이디 찾기
    List<String> findUserId(String email);
    // 회원 찾기
    MyPageForm findMember(String userId);
    // 방문횟수 증가
    void hitsUp(String userId);
    // 메일 전송(임시 비밀번호 생성)
    void sendMailForCreateTemporaryPassword(String userId, String FromEmail, String toEmail) throws MessagingException;
    // 비밀번호 변경
    Boolean changePassword(String userId, String email, String oldPassword, String newPassword);
    Member validationMemberByUserIdAndEmail(String userId, String toEmail);
}
