package quokka.todayflowers.global.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

public class BasicException extends IllegalArgumentException {

    public BasicException(String s) {
        super(s);
    }

    public BasicException(String message, Throwable cause) {
        super(message, cause);
    }
}
