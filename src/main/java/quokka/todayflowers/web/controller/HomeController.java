package quokka.todayflowers.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import quokka.todayflowers.domain.repository.MemberRepository;
import quokka.todayflowers.domain.service.MemberService;

import java.security.Principal;
import java.util.Map;

@Controller
@Slf4j
public class HomeController {

    // 홈
    @GetMapping("/")
    public String home() {

        return "home";
    }

    @GetMapping("/error?continue")
    public String error() {

        log.error("에러페이지를 받음");

        return "redirect:/";
    }
}
