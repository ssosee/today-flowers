package quokka.todayflowers.web.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginForm {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String userId;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    public LoginForm(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
