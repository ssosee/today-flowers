package quokka.todayflowers.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginForm {
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "아이디는 영문 대/소문자와 숫자만 가능합니다.")
    private String userId;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    public LoginForm(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
