package quokka.todayflowers.global.exception.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import quokka.todayflowers.global.exception.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@ControllerAdvice
public class ControllerExHandler {
    // 공통
    @ExceptionHandler(CommonException.class)
    public String exWithdrawalHandler(CommonException e) {
        String encode = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
        return "redirect:/common?error=true&exception="+encode;
    }

    // 오늘의 꽃 예외 처리
    @ExceptionHandler(TodayException.class)
    public ModelAndView exTodayHandler(TodayException e) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("flower/today/todayFlower");
        mv.addObject("error", true);
        mv.addObject("exception", e.getMessage());

        return mv;
    }

    // 생일의 꽃 예외 처리
    @ExceptionHandler(BirthException.class)
    public String exBirthHandler(BirthException e, RedirectAttributes redirectAttributes) {
        String[] errorMessage = e.getMessage().split("_");
        redirectAttributes.addAttribute("birth");
        redirectAttributes.addAttribute("exception", errorMessage[1]);
        redirectAttributes.addAttribute("error", true);

        return "redirect:/today-flower/birth";
    }

    // 꽃말의 꽃 예외 처리
    @ExceptionHandler(LangException.class)
    public String exLangHandler(LangException e) {
        String encode = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
        return "redirect:/today-flower/lang-list?error=true&exception="+encode;
    }

    // 이름의 꽃 예외 처리
    @ExceptionHandler(NameException.class)
    public String exNameHandler(NameException e) {
        String encode = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
        return "redirect:/today-flower/name-list?error=true&exception="+encode;
    }

    // 사용자 좋아요 목록 예외 처리
    @ExceptionHandler(FlowerLikeException.class)
    public ModelAndView exFlowerLikeHandler(FlowerLikeException e) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("member/likeFlowerList");
        mv.addObject("error", true);
        mv.addObject("exception", e.getMessage());

        return mv;
    }

    // 회원가입
    @ExceptionHandler(JoinException.class)
    public String exJoinHandler(JoinException e) {
        String encode = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
        return "redirect:/user/signup?error=true&exception="+encode;
    }

    // 비밀번호 변경
    @ExceptionHandler(ChangePasswordException.class)
    public String exChangePasswordHandler(ChangePasswordException e) {
        String encode = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
        return "redirect:/user/change-password?error=true&exception="+encode;
    }

    // 이메일 변경
    @ExceptionHandler(ChangeEmailException.class)
    public String exChangeEmailHandler(ChangeEmailException e) {
        String encode = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
        return "redirect:/user/change-email?error=true&exception="+encode;
    }
}
