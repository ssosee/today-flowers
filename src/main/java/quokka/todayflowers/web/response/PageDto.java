package quokka.todayflowers.web.response;

import lombok.Builder;
import lombok.Data;

@Data
public class PageDto {
    // 범위 내 시작 페이지
    private Integer startPage;
    // 범위 내 끝 페이지
    private Integer endPage;

    @Builder
    public PageDto(Integer startPage, Integer endPage) {
        this.startPage = startPage;
        this.endPage = endPage;
    }
}
