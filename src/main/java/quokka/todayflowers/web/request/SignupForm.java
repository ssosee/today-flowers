package quokka.todayflowers.web.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
public class SignupForm {
    @NotBlank(message = "아이디를 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "아이디는 영문 대/소문자와 숫자만 가능합니다.")
    private String userId;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "8자리 이상, 영어 대/소문자, 숫자, 특수문자가 각각 1개 이상 포함해야 합니다.")
    private String password;
    @Email(message = "이메일을 확인해주세요.")
    private String email;

    public SignupForm(String userId, String password, String email) {
        this.userId = userId;
        this.password = password;
        this.email = email;
    }
}
