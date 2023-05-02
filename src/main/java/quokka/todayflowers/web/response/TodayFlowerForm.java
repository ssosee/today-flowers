package quokka.todayflowers.web.response;

import lombok.Data;

import java.util.List;

/**
 * 오늘의 꽃 폼
 */
@Data
public class TodayFlowerForm {
    private String name; // 이름
    private String flowerLang; // 꽃말
    private String description; // 설명
    private Long totalLike; // 총 좋아요 수
    private Long hits; // 총 조회수
    private List<String> photoPath; // 사진 경로
}
