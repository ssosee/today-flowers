package quokka.todayflowers.web.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class FindUserIdForm {
    @Email(message = "이메일을 확인해주세요.")
    private String email;

    public FindUserIdForm(String email) {
        this.email = email;
    }
}
