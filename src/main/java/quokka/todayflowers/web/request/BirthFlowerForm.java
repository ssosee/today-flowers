package quokka.todayflowers.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BirthFlowerForm {
    @NotNull(message = "생년월일을 입력해주세요.")
    @Pattern(regexp = "^\\d{6}$", message = "생년월일을 확인해주세요.")
    private String birth;
}
