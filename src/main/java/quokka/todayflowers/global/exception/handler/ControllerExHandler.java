package quokka.todayflowers.global.exception.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import quokka.todayflowers.global.exception.BirthException;
import quokka.todayflowers.global.exception.FlowerLikeException;
import quokka.todayflowers.global.exception.LangException;
import quokka.todayflowers.global.exception.NameException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@ControllerAdvice
public class ControllerExHandler {

    // 생일의 꽃 예외 처리
    @ExceptionHandler(BirthException.class)
    public String exBirthHandler(BirthException e) {
        String encode = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
        return "redirect:/today-flower/birth?error=true&exception="+encode;
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
}
