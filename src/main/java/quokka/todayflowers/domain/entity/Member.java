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
    private LocalDateTime lockDate; // 계정 잠긴 시간
    private Integer loginFailCount; // 로그인 실패 횟수
    @Enumerated(EnumType.STRING)
    private SocialType socialType; // 소셜 로그인 타입
    private String socialName; // 소셜 로그인 name
    @OneToMany(mappedBy = "member")
    private List<FlowerLike> flowerLikes = new ArrayList<>();

    public static Member createNewMember(String userId, String password, String email) {
        Member member = new Member();

        member.userId = userId;
        member.password = password;
        member.email = email;
        member.role = ConstMember.ROLE;
        member.hits = 0L;
        member.loginFailCount = 0;
        member.socialType = SocialType.NONE;

        return member;
    }

    // 소셜 로그인 회원 생성
    public static Member createSocialMember(String userId, String socialName, String password, String email, SocialType socialType) {
        Member member = createNewMember(userId, password, email);
        member.socialType = socialType;
        member.socialName = socialName;

        return member;
    }

    public void changeHits(Long hits) {
        this.hits += hits;
    }

    // 로그인
    public void setLogin() {
        this.hits += 1L;
        this.loginDate = LocalDateTime.now();
        initLoginFailCount();
    }

    // 비밀번호 변경
    public void changePassword(String password) {
        this.password = password;
    }

    // 로그인 실패 횟수 증가
    public void increaseLoginFailCount() {
        // 로그인 실패 횟수가 5회 미만이면 실패 횟수 증가
        if(this.loginFailCount < 5) {
            this.loginFailCount += 1;
        } else {
            this.lockDate = LocalDateTime.now();
        }
    }

    // 로그인 실패 카운트 초기화
    public void initLoginFailCount() {
        this.loginFailCount = 0;
        this.lockDate = null;
    }

    // 이메일 변경
    public void changeEmail(String email) {
        this.email = email;
    }

    // 연관관계 편의 메소드
    public void changeFlowerLike(FlowerLike flowerLike) {
        flowerLikes.add(flowerLike);
        flowerLike.setMember(this);
    }

    public void removeFlowerLike(FlowerLike flowerLike) {
        flowerLikes.remove(flowerLike);
        flowerLike.setMember(null);
    }
}
