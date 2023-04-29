package quokka.todayflowers.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import quokka.todayflowers.global.constant.ConstMember;

@ControllerAdvice
public class BasicExHandler {
    @ExceptionHandler(BasicException.class)
    public ModelAndView exHandler(BasicException e) {
        ModelAndView mv = new ModelAndView();

        return mv;
    }

}
