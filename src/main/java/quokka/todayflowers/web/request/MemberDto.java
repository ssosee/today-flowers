package quokka.todayflowers.web.request;

import lombok.Data;

@Data
public class MemberDto {
    private String userId;
    private String password;

    public MemberDto(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
