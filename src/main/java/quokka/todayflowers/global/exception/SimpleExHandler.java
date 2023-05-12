package quokka.todayflowers.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@ControllerAdvice
public class SimpleExHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> kakaoExHandler(HttpClientErrorException e, Model model) {

        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> exHandler(RuntimeException e) {
        log.error("에러 발생={}", e.getMessage());

        return ResponseEntity.badRequest().build();
    }
}
