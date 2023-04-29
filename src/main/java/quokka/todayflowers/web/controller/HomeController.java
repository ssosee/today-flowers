package quokka.todayflowers.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import quokka.todayflowers.domain.repository.MemberRepository;
import quokka.todayflowers.domain.service.MemberService;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberService memberService;
    @GetMapping("/home")
    public String home() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String userId = authentication.getName();

        // 회원이면
        if(!"anonymousUser".equalsIgnoreCase(userId)) {
            // 방문 횟수 증가
            memberService.hitsUp(userId);
        }

        return "home";
    }
}
