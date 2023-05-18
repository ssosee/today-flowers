package quokka.todayflowers.web.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class BasicFlowerForm {
    private List<FlowerListForm> flowerList;
    private Integer currentPage;
    private Integer totalPages;
    private Integer startPage;
    private Integer endPage;

    @Builder
    public BasicFlowerForm(List<FlowerListForm> flowerList, Integer currentPage, Integer totalPages, Integer startPage, Integer endPage) {
        this.flowerList = flowerList;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.startPage = startPage;
        this.endPage = endPage;
    }
}
