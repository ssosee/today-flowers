package quokka.todayflowers.web.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeEmailForm {
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일을 확인해주세요.")
    private String email;
}
