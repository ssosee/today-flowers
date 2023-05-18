package quokka.todayflowers.global.config.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SimpleOAuth2LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 이전 요청에 대한 정보
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        // 로그인 전 방문한 자원(URL)이 없는 경우
        // 즉, 어떤 자원에 접근하다가 인증 예외가 발생한 경우 아닌 경우
        if(savedRequest == null) {
            // 홈으로 보낸다.
            response.sendRedirect("/");
            return;
        }

        // 어떤 자원에 접근하다가 로그인 됨
        String redirectUrl = savedRequest.getRedirectUrl();
        // 로그인 전에 방문한 곳으로 보낸다.
        response.sendRedirect(redirectUrl);
    }
}
