package quokka.todayflowers.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LangFlowerForm {
    @Pattern(regexp = "/^[ㄱ-ㅎㅏ-ㅣ가-힣]*$/")
    private String lang = "";
}
