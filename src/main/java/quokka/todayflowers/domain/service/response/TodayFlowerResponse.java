package quokka.todayflowers.domain.service.response;

import lombok.Builder;
import lombok.Data;
import quokka.todayflowers.domain.entity.Flower;
import quokka.todayflowers.domain.entity.FlowerPhoto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 오늘의 꽃 폼
 */
@Data
public class TodayFlowerResponse {
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
    private TodayFlowerResponse(Long flowerId, String name, String flowerLang, String description, Long totalLike, Long hits, List<String> photoPath, String userId, Boolean like) {
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

    public static TodayFlowerResponse of(Flower flower, String userId, boolean like) {
        // dto로 변환
        return TodayFlowerResponse.builder()
                .flowerId(flower.getId())
                .flowerLang(flower.getFlowerLang())
                .photoPath(flower.getFlowerPhotos().stream()
                        .map(FlowerPhoto::getPath).collect(Collectors.toList()))
                .totalLike(flower.getTotalLike())
                .name(flower.getName())
                .hits(flower.getHits())
                .description(flower.getDescription())
                .userId(userId)
                .like(like)
                .build();
    }
}
