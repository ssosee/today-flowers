package quokka.todayflowers.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FlowerPhoto extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String path = "/image/flower-today.jpg";
    private String reference;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flower_id")
    private Flower flower;

    public static FlowerPhoto createFlowerPhoto(String path, String reference) {
        FlowerPhoto flowerPhoto = new FlowerPhoto();

        flowerPhoto.path = path;
        flowerPhoto.reference = reference;

        return flowerPhoto;
    }

    public void setFlower(Flower flower) {
        this.flower = flower;
    }
}
