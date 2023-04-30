package quokka.todayflowers.domain.service;

import quokka.todayflowers.domain.entity.Member;
import quokka.todayflowers.web.response.MyPageForm;

import java.util.List;

public interface MemberService {
    // 회원가입
    Boolean join(String userId, String password, String email);
    // 로그인
    Boolean login(String userId, String password);
    // 로그아웃
    void logout(String userId);
    // 회원탈퇴
    void withdrawalMember(String userId);
    // 아이디 찾기
    List<String> findUserId(String email);
    // 회원 찾기
    MyPageForm findMember(String userId);
    // 방문횟수 증가
    void hitsUp(String userId);
}
