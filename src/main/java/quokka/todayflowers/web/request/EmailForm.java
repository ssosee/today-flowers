package quokka.todayflowers.web.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class EmailForm {
    @Email(message = "이메일 형식을 확인해주세요.")
    private String email;

    public EmailForm(String email) {
        this.email = email;
    }
}
