package quokka.todayflowers.global.config.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import quokka.todayflowers.domain.entity.Member;
import quokka.todayflowers.domain.repository.MemberRepository;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional
public class SimpleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        // 로그인 성공한 상태이기 때문에 반드시 객체가 존재함
        Member findMember = optionalMember.get();

        // 로그인 실패 초기화
        findMember.initLoginFailCount();

        response.sendRedirect("/home");
    }
}
