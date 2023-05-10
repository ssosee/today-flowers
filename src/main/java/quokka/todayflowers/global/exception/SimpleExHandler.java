package quokka.todayflowers.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@ControllerAdvice
public class SimpleExHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public String kakaoExHandler(HttpClientErrorException e, Model model) {

        model.addAttribute(e.getMessage());
        return "/error/4xx";
    }
}
