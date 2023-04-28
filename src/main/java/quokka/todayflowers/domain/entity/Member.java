package quokka.todayflowers.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import quokka.todayflowers.global.constant.ConstMember;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String password;
    private String email;
    private String role;
    @OneToMany(mappedBy = "member")
    private List<FlowerLike> flowerLikes = new ArrayList<>();

    public static Member createNewMember(String userId, String password, String email) {
        Member member = new Member();
        member.userId = userId;
        member.password = password;
        member.email = email;
        member.role = ConstMember.ROLE;

        return member;
    }
}
