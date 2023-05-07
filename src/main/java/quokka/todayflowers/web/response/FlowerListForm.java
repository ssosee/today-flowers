package quokka.todayflowers.web.response;

import lombok.Builder;
import lombok.Data;

@Data
public class FlowerListForm {
    // 번호
    private Long id;
    // 이름
    private String name;
    // 꽃말
    private String lang;
    // 사진
    private String path;
    // 좋아요
    private Long totalLike;

    @Builder
    public FlowerListForm(Long id, String name, String lang, String path, Long totalLike) {
        this.id = id;
        this.name = name;
        this.lang = lang;
        this.path = path;
        this.totalLike = totalLike;
    }
}
