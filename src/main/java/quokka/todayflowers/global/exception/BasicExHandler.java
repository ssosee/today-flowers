package quokka.todayflowers.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import quokka.todayflowers.global.constant.ConstFlower;
import quokka.todayflowers.global.constant.ConstMember;

@RestControllerAdvice
public class BasicExHandler {
    @ExceptionHandler(BasicException.class)
    public ResponseEntity<String> exHandler(BasicException e) {
        return new ResponseEntity<>(ConstFlower.FLOWER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

}
