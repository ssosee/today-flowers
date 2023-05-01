package quokka.todayflowers.web.response;

import lombok.Builder;
import lombok.Data;

@Data
public class MyPageForm {
    private String userId;
    private String email;
    private Long hits;
    private String joinDate;
    private Long likeCount;

    public MyPageForm() {
        this.userId = "검색 결과가 없습니다.";
        this.email = "검색 결과가 없습니다.";
        this.hits = 0L;
        this.joinDate = "검색 결과가 없습니다.";
        this.likeCount = 0L;
    }

    @Builder
    public MyPageForm(String userId, String email, Long hits, String joinDate, Long likeCount) {
        this.userId = userId;
        this.email = email;
        this.hits = hits;
        this.joinDate = joinDate;
        this.likeCount = likeCount;
    }
}
