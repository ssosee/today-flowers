package quokka.todayflowers.global.config.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import quokka.todayflowers.domain.entity.Member;
import quokka.todayflowers.domain.repository.MemberRepository;
import quokka.todayflowers.global.constant.ConstMember;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional
public class SimpleAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errMessage = "";

        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        // 아이디 또는 비밀번호 입력을 안했을 경우
        if(!StringUtils.hasText(userId) || !StringUtils.hasText(password)) {
            errMessage = ConstMember.LOGIN_BLANK;
            // 에러 메시지 UTF_8로 변환
            errMessage = URLEncoder.encode(errMessage, StandardCharsets.UTF_8);
            // 에러 메시지 url에 포함
            response.sendRedirect("/user/login?error=true&exception="+errMessage);
            return;
        }

        // 아이디로 회원 조회
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        Member findMember = optionalMember.orElse(null);

        // 존재하지 않는 회원이 없는 경우
        if(findMember == null) {
            errMessage = ConstMember.LOGIN_FAIL;
            // 에러 메시지 UTF_8로 변환
            errMessage = URLEncoder.encode(errMessage, StandardCharsets.UTF_8);
            // 에러 메시지 url에 포함
            response.sendRedirect("/user/login?error=true&exception="+errMessage);
            return;
        }

        // 아이디 혹은 비밀번호가 틀릴 경우
        if(exception instanceof BadCredentialsException) {
            // 회원이 존재하면
            if(findMember != null) {
                // 로그인 실패 횟수 증가
                findMember.increaseLoginFailCount();
                // 로그인 실패 횟수가 5회 이상이면
                if(findMember.getLoginFailCount() >= 5) {
                    errMessage = ConstMember.LOCK_MEMBER;
                } else {
                    errMessage = "비밀번호 "+findMember.getLoginFailCount()+"회 오류 입니다. 5회 이상 비밀번호 오류시 계정이 잠깁니다.";
                }
            }
            // 회원이 존재하지 않으면
            else {
                errMessage = ConstMember.LOGIN_FAIL;
            }

        }
        // 로그인 실패 횟수가 5회 이상이면
        else if (exception instanceof InternalAuthenticationServiceException) {
            if(findMember.getLoginFailCount() >= 5) {
                errMessage = ConstMember.LOCK_MEMBER;
            }

        } else {
            errMessage = "관리자에게 문의 주세요.";
        }

        // 에러 메시지 UTF_8로 변환
        errMessage = URLEncoder.encode(errMessage, StandardCharsets.UTF_8);
        // 에러 메시지 url에 포함
        response.sendRedirect("/user/login?error=true&exception="+errMessage);
    }
}
