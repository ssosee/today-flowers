package quokka.todayflowers.web.controller;

import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.extras.springsecurity6.util.SpringSecurityContextUtils;
import quokka.todayflowers.domain.entity.Member;
import quokka.todayflowers.domain.service.MemberService;
import quokka.todayflowers.global.constant.ConstMember;
import quokka.todayflowers.web.request.EmailForm;
import quokka.todayflowers.web.request.LoginForm;
import quokka.todayflowers.web.request.SignupForm;
import quokka.todayflowers.web.response.MyPageForm;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {
    private final MemberService memberService;

    // 로그인
    @GetMapping("/login")
    public String loginPage(@ModelAttribute("form") LoginForm form,
                            @RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "exception", required = false) String exception,
                            @RequestParam(value = "status", required = false) String status,
                            Model model) {

        // 에러 정보를 model에 저장
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        model.addAttribute("status", status);

        return "/member/login";
    }

    // 회원 가입
    @GetMapping("/signup")
    public String signupPage(@ModelAttribute("form") SignupForm form) {
        return "/member/signup";
    }

    // 회원 가입
    @PostMapping("/signup")
    public String signup(@Validated @ModelAttribute("form") SignupForm form, BindingResult bindingResult) {

        // 요청메시지 검증
        if(bindingResult.hasErrors()) {
            return "/member/signup";
        }

        // 회원가입 로직 수행
        Boolean serviceResult = memberService.join(form.getUserId(), form.getPassword(), form.getEmail());

        // 회원가입이 불가능 하면
        if(!serviceResult) {
            // 글로벌 오류 생성
            bindingResult.reject("duplicate_user_id", ConstMember.DUPLICATE_USER_ID);
            return "/member/signup";
        }

        return "redirect:/home";
    }

    // 회원 탈퇴
    @PostMapping("/withdrawal")
    public String withdrawal(@ModelAttribute("userId") String userId) {
        memberService.withdrawalMember(userId);

        return "redirect:/user/signup";
    }

    // 내정보
    @GetMapping("/mypage")
    public String myPage(Model model) {
        // 스프링시큐리티 컨테스트에서 userId 꺼내기
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String userId = authentication.getName();

        // 회원 조회
        MyPageForm form = memberService.findMember(userId);
        model.addAttribute("form", form);

        return "/member/mypage";
    }

    // 회원아이디 찾기
    @GetMapping("/find-userId")
    public String findUserIdPage(@ModelAttribute("form") EmailForm form) {
        return "/member/findUser";
    }

    // howisitgoing@kakao.com
    // 회원아이디 찾기
    @PostMapping("/find-userId")
    public String findUserId(@Validated @ModelAttribute("form") EmailForm form,
                             BindingResult bindingResult,
                             Model model) {

        if(bindingResult.hasErrors()) {
            return "/member/findUser";
        }

        // 회원이 없으면
        List<String> userIds = memberService.findUserId(form.getEmail());
        if(userIds.size() == 0) {
            bindingResult.reject("email_not_found", ConstMember.MEMBER_NOT_FOUND);
            return "/member/findUser";
        }

        model.addAttribute("userIds", userIds);

        return "/member/findUser";
    }

    // 비밀번호 초기화
}
