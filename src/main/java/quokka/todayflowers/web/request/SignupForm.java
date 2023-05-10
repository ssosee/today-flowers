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
    @Pattern(regexp = "^(?=.*[a-zA-Z])[a-zA-Z0-9]*$", message = "아이디는 영문 대/소문자 또는 영문과 숫자 조합만 가능합니다.")
    private String userId;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,14}$", message = "비밀번호는 8 ~ 14자리, 영어 대/소문자, 숫자, 특수문자가 각각 1개 이상 포함해야 합니다.")
    private String password1;
    private String password2;
    @Email(message = "이메일을 확인해주세요.")
    private String email;
}
