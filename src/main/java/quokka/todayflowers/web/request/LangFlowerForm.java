package quokka.todayflowers.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LangFlowerForm {
    @Pattern(regexp = "\"^[가-힣]*$\"\n", message = "한글만 입력 가능합니다.")
    private String lang;
}
