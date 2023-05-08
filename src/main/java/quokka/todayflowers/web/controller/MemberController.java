package quokka.todayflowers.web.controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import quokka.todayflowers.domain.service.MemberService;
import quokka.todayflowers.global.common.SimpleCommonMethod;
import quokka.todayflowers.global.constant.ConstMember;
import quokka.todayflowers.web.request.*;
import quokka.todayflowers.web.response.MyPageForm;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {
    private final MemberService memberService;
    private final SimpleCommonMethod simpleCommonMethod;

    @Value("${spring.mail.username}")
    private String adminEmail;

    // 로그인
    @GetMapping("/login")
    public String loginPage(@ModelAttribute("form") LoginForm form,
                            @RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "exception", required = false) String exception,
                            Model model) {

        // 에러 정보를 model에 저장
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "member/login";
    }

    // 회원 가입
    @GetMapping("/signup")
    public String signup(@ModelAttribute("form") SignupForm form) {
        return "/member/signup";
    }

    // 회원 가입
    @PostMapping("/signup")
    public String signup(@Validated @ModelAttribute("form") SignupForm form, BindingResult bindingResult) {

        // 요청메시지 검증
        if(bindingResult.hasErrors()) {
            return "member/signup";
        }

        // 회원가입 로직 수행
        Boolean serviceResult = memberService.join(form.getUserId(), form.getPassword(), form.getEmail());

        // 회원가입이 불가능 하면
        if(!serviceResult) {
            // 글로벌 오류 생성
            bindingResult.reject("duplicate_user_id", ConstMember.DUPLICATE_USER_ID);
            return "member/signup";
        }

        return "redirect:/home";
    }

    // 회원 탈퇴
    @PostMapping("/withdrawal")
    public String withdrawal(@ModelAttribute("userId") String userId, BindingResult bindingResult) {
        // 회원 탈퇴 로직
        Boolean serviceResult = memberService.withdrawalMember(userId);

        if(!serviceResult) {
            bindingResult.reject("user_not_found", ConstMember.MEMBER_NOT_FOUND);
        }

        return "redirect:/user/signup";
    }

    // 내정보
    @GetMapping("/mypage")
    public String myPage(Model model) {
        // 스프링시큐리티 컨테스트에서 userId 꺼내기
        String userId = simpleCommonMethod.getCurrentUserId();

        // 회원 조회
        MyPageForm form = memberService.findMember(userId);
        model.addAttribute("form", form);

        return "member/mypage";
    }

    // 회원아이디 찾기
    @GetMapping("/find-userId")
    public String findUserId(@ModelAttribute("form") FindUserIdForm form) {
        return "/member/findUserId";
    }

    // 회원아이디 찾기
    @PostMapping("/find-userId")
    public String findUserId(@Validated @ModelAttribute("form") FindUserIdForm form,
                             BindingResult bindingResult,
                             Model model) {

        // 요청 데이터 검증
        if(bindingResult.hasErrors()) {
            return "member/findUserId";
        }

        // 회원이 없으면
        List<String> userIds = memberService.findUserId(form.getEmail());
        if(userIds.size() == 0) {
            bindingResult.reject("email_not_found", ConstMember.MEMBER_NOT_FOUND);
            return "member/findUserId";
        }

        model.addAttribute("userIds", userIds);

        return "member/findUserId";
    }

    // 임시 비밀번호 발급
    @GetMapping("/find-password")
    public String createTemporaryPassword(@ModelAttribute("form") FindPasswordForm form) {
        return "member/findUserPassword";
    }

    // 임시 비밀번호 발급
    @PostMapping("/send-email")
    public String createTemporaryPassword(@Validated @ModelAttribute("form") FindPasswordForm form,
                            BindingResult bindingResult,
                            Model model) throws MessagingException {

        // 요청 데이터 검증
        if(bindingResult.hasErrors()) {
            return "member/findUserPassword";
        }

        // 메일 전송(임시 비밀번호)
        Boolean serviceResult = memberService.sendMailForCreateTemporaryPassword(form.getUserId(), adminEmail, form.getEmail());
        if (!serviceResult) {
            bindingResult.reject("send_email_fail", ConstMember.SEND_EMAIL_FAIL);
            return "member/findUserPassword";
        }

        model.addAttribute("sendEmailSuccess", ConstMember.SEND_EMAIL_SUCCESS);
        return "member/findUserPassword";
    }

    // 비밀번호 변경
    @GetMapping("/change-password")
    public String changePassword(@ModelAttribute("form") ChangePasswordForm form) {
        return "/member/changePassword";
    }

    @PostMapping("/change-password")
    public String changePassword(@Validated @ModelAttribute("form") ChangePasswordForm form,
                                 BindingResult bindingResult,
                                 Model model) {

        if(bindingResult.hasErrors()) {
            return "member/changePassword";
        }

        Boolean serviceResult = memberService.changePassword(form.getUserId(), form.getEmail(), form.getOldPassword(), form.getNewPassword());
        if(!serviceResult) {
            bindingResult.reject("change_password_fail", ConstMember.CHANGE_PASSWORD_FAIL);
            return "member/changePassword";
        }


        model.addAttribute("changePasswordSuccess", ConstMember.CHANGE_PASSWORD_SUCCESS);
        return "member/changePassword";
    }

    // 로그인 만료
    @GetMapping("/invalid")
    public String invalid(@ModelAttribute("form") LoginForm form, Model model) {

        // 에러 정보를 model에 저장
        model.addAttribute("error", true);
        model.addAttribute("exception", ConstMember.INVALID_LOGIN);

        return "member/login";
    }
}
