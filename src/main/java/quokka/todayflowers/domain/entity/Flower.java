package quokka.todayflowers.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Flower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String flowerLang;
    @Lob
    private String description;
    @Column(name = "`MONTH`")
    private Integer month;
    @Column(name = "`DAY`")
    private Integer day;
    private String reference;
    private Long totalLike;
    private Long hits; // 조회수
    @OneToMany(mappedBy = "flower")
    private List<FlowerPhoto> flowerPhotos = new ArrayList<>();
}
