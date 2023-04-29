package quokka.todayflowers.web.response;

import lombok.Builder;
import lombok.Data;

@Data
public class MyPageForm {
    private String userId;
    private String email;
    private Long hits;
    private String joinDate;
    @Builder
    public MyPageForm(String userId, String email, Long hits, String joinDate) {
        this.userId = userId;
        this.email = email;
        this.hits = hits;
        this.joinDate = joinDate;
    }
}
