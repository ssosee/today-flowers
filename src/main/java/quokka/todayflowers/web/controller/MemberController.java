package quokka.todayflowers.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import quokka.todayflowers.domain.service.MemberService;
import quokka.todayflowers.global.constant.ConstMember;
import quokka.todayflowers.web.request.LoginForm;
import quokka.todayflowers.web.request.SignupForm;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/login")
    public String loginPage(@ModelAttribute("form") LoginForm form,
                            @RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "exception", required = false) String exception,
                            Model model) {

        // 에러 정보를 model에 저장
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "/member/login";
    }

    @GetMapping("/signup")
    public String signupPage(@ModelAttribute("form") SignupForm form) {
        return "/member/signup";
    }

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
}
