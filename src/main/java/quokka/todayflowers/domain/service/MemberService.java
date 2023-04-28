package quokka.todayflowers.domain.service;

import quokka.todayflowers.domain.entity.Member;

public interface MemberService {
    // 회원가입
    Boolean join(String userId, String password, String email);
    // 로그인
    Boolean login(String userId, String password);
    // 로그아웃
    void logout(String userId);
    // 회원탈퇴
    void withdrawalMember(String userId, String password, String email);
    // 아이디 찾기
    String findUserId(String email);
}
