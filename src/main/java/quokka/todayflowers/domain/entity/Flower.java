package quokka.todayflowers.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Flower extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String flowerLang;
    @Lob @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    @Column(name = "`MONTH`")
    private Integer month;
    @Column(name = "`DAY`")
    private Integer day;
    private String reference;
    @Column(nullable = false)
    private Long totalLike;
    @Column(nullable = false)
    private Long hits; // 조회수
    @OneToMany(mappedBy = "flower")
    private List<FlowerPhoto> flowerPhotos = new ArrayList<>();

    @Builder
    private Flower(String name, String flowerLang, String description, Integer month, Integer day, String reference, Long totalLike, Long hits, List<FlowerPhoto> flowerPhotos) {
        this.name = name;
        this.flowerLang = flowerLang;
        this.description = description;
        this.month = month;
        this.day = day;
        this.reference = reference;
        this.totalLike = totalLike;
        this.hits = hits;
        this.flowerPhotos = flowerPhotos;
    }

    public static Flower createFlower(String name, String flowerLang, String description, Integer month, Integer day, String reference) {

        Flower flower = new Flower();

        flower.name = name;
        flower.flowerLang = flowerLang;
        flower.description = description;
        flower.month = month;
        flower.day = day;
        flower.reference = reference;
        flower.hits = 0L;
        flower.totalLike = 0L;

        return flower;
    }

    public static Flower create(String name, String flowerLang, String description, Integer month, Integer day, String reference, FlowerPhoto flowerPhoto) {
        return Flower.builder()
                .name(name)
                .flowerLang(flowerLang)
                .description(description)
                .month(month)
                .day(day)
                .reference(reference)
                .hits(0L)
                .totalLike(0L)
                .flowerPhotos(List.of(flowerPhoto))
                .build();
    }

    // 조회수 증가 로직
    public void increaseHits() {
        this.hits += 1;
    }

    // 좋아요 처리 로직
    public void totalLikeLogic(Boolean like) {
        if(this.totalLike >= 0) {
            // 좋아요 취소
            if(this.totalLike > 0 && !like) {
                this.totalLike -= 1;
            }
            // 좋아요
            else {
                this.totalLike += 1;
            }
        }
    }

    // 연관관계 편의 메소드
    public void changeFlowerPhoto(FlowerPhoto flowerPhoto) {
        flowerPhotos.add(flowerPhoto);
        flowerPhoto.setFlower(this);
    }
}
