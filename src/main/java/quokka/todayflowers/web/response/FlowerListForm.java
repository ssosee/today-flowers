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

    // 전체 데이터 수
    private Integer number;
    // 페이지 번호
    private Long totalElements;
    // 전체 페이지 번호
    private Integer totalPage;

    @Builder
    public FlowerListForm(Long id, String name, String lang, String path, Long totalLike, Integer number, Long totalElements, Integer totalPage) {
        this.id = id;
        this.name = name;
        this.lang = lang;
        this.path = path;
        this.totalLike = totalLike;
        this.number = number;
        this.totalElements = totalElements;
        this.totalPage = totalPage;
    }
}
