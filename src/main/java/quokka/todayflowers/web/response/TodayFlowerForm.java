package quokka.todayflowers.web.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 오늘의 꽃 폼
 */
@Data
public class TodayFlowerForm {
    private Long flowerId;
    private String name; // 이름
    private String flowerLang; // 꽃말
    private String description; // 설명
    private Long totalLike = 0L; // 총 좋아요 수
    private Long hits = 0L; // 총 조회수
    private List<String> photoPath; // 사진 경로

    // 회원 기본키
    private String userId;
    // 좋아요 상태
    private Boolean like;

    @Builder
    public TodayFlowerForm(Long flowerId, String name, String flowerLang, String description, Long totalLike, Long hits, List<String> photoPath, String userId, Boolean like) {
        this.flowerId = flowerId;
        this.name = name;
        this.flowerLang = flowerLang;
        this.description = description;
        this.totalLike = totalLike;
        this.hits = hits;
        this.photoPath = photoPath;
        this.userId = userId;
        this.like = like;
    }
}
