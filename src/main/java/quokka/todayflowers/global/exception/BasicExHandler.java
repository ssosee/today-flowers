package quokka.todayflowers.global.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class BasicExHandler {
    @ExceptionHandler(BasicException.class)
    public ModelAndView exHandler(BasicException e, String path) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName(path);
        mv.addObject(e.toString());

        return mv;
    }
}
