package quokka.todayflowers.web.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
public class SignupForm {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String userId;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    @Email(message = "이메일 형식을 확인해주세요.")
    private String email;

    public SignupForm(String userId, String password, String email) {
        this.userId = userId;
        this.password = password;
        this.email = email;
    }
}
