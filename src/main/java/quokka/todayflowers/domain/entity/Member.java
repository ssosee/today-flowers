package quokka.todayflowers.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import quokka.todayflowers.global.constant.ConstMember;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String password;
    private String email;
    private String role;
    private Long hits; // 사이트 방문 횟수
    private LocalDateTime loginDate; // 로그인 시간
    @OneToMany(mappedBy = "member")
    private List<FlowerLike> flowerLikes = new ArrayList<>();

    public static Member createNewMember(String userId, String password, String email) {
        Member member = new Member();
        member.userId = userId;
        member.password = password;
        member.email = email;
        member.role = ConstMember.ROLE;
        member.hits = 0L;

        return member;
    }

    public void changeHits(Long hits) {
        this.hits += hits;
    }

    public void setLogin() {
        this.hits += 1L;
        this.loginDate = LocalDateTime.now();
    }

    public void changePassword(String password) {
        this.password = password;
    }

    // 연관관계 편의 메소드
    public void changeFlowerLike(FlowerLike flowerLike) {
        flowerLikes.add(flowerLike);
        flowerLike.setMember(this);
    }
}
