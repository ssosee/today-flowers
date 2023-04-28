package quokka.todayflowers.global.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import quokka.todayflowers.global.constant.ConstMember;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SimpleAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errMessage = "";

        // request에 메시지가 없는 경우
        if("POST".equalsIgnoreCase(request.getMethod())) {
            BufferedReader reader = request.getReader();
            String str = reader.readLine();

            if(str == null) errMessage = ConstMember.LOGIN_BLANK;
        }
        else if(exception instanceof BadCredentialsException) {
            errMessage = ConstMember.LOGIN_FAIL;
        } else {
            errMessage = "관리자에게 문의 주세요.";
        }

        // 에러 메시지 UTF_8로 변환
        errMessage = URLEncoder.encode(errMessage, StandardCharsets.UTF_8);
        // 에러 메시지 url에 포함
        response.sendRedirect("/user/login?error=true&exception="+errMessage);
    }
}
