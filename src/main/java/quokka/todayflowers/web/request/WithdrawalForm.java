package quokka.todayflowers.web.request;

import lombok.Data;

@Data
public class WithdrawalForm {
    private String userId;
    private String socialId;
}
