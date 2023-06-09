package quokka.todayflowers.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
