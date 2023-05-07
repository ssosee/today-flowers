package quokka.todayflowers.web.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class NameFlowerForm {
    @Pattern(regexp = "^[가-힣]*$", message = "한글만 입력 가능합니다.")
    private String name;
}
