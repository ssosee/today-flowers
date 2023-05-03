package quokka.todayflowers.global.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SimpleCommonMethod {
    public String getCurrentUserId() {
        // 스프링시큐리티 컨테스트에서 userId 꺼내기
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String userId = authentication.getName();

        return userId;
    }
}
