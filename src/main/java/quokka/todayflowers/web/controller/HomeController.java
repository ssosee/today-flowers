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
public class HomeController {

    // 홈
    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
