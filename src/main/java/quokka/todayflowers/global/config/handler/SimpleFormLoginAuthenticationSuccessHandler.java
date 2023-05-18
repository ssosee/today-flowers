package quokka.todayflowers.global.config.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
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
public class SimpleFormLoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        // 로그인 성공한 상태이기 때문에 반드시 객체가 존재함
        Member findMember = optionalMember.get();

        // 로그인 실패 초기화
        findMember.initLoginFailCount();

        // 이전 요청에 대한 정보
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        // 로그인 전 방문한 URL이 없는 경우
        // 즉, 어떤 자원에 접근하다가 예외가 발생한 경우 아닌 경우
        if(savedRequest == null) {
            // Home으로 보낸다.
            response.sendRedirect("/");
            return;
        }

        // 어떤 자원에 접근하다가 인증 예외가 발생한 경우
        String redirectUrl = savedRequest.getRedirectUrl();
        // 어떤 자원으로 보내준다.
        response.sendRedirect(redirectUrl);
    }
}
