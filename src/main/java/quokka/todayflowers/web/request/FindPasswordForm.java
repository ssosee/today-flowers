package quokka.todayflowers.web.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class FindPasswordForm {
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "아이디는 영문 대/소문자와 숫자만 가능합니다.")
    private String userId;
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일을 확인해주세요.")
    private String email;
    private String authenticationNumber;

    public FindPasswordForm(String userId, String email, String authenticationNumber, String password) {
        this.userId = userId;
        this.email = email;
        this.authenticationNumber = authenticationNumber;
    }
}
