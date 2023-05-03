package quokka.todayflowers.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자가 좋아요를 누른 꽃
 * 좋아요 바구니(장바구니)
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FlowerLike extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flower_id")
    private Flower flower;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static FlowerLike createFlowerLike(Flower flower, Member member) {
        FlowerLike flowerLike = new FlowerLike();
        flowerLike.flower = flower;
        flowerLike.member = member;

        return flowerLike;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
