package quokka.todayflowers.domain.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import quokka.todayflowers.domain.entity.Flower;
import quokka.todayflowers.domain.entity.FlowerLike;
import quokka.todayflowers.domain.entity.Member;
import quokka.todayflowers.domain.repository.FlowerLikeRepository;
import quokka.todayflowers.domain.repository.FlowerRepository;
import quokka.todayflowers.domain.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class withdrawalMemberQueryTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    FlowerLikeRepository flowerLikeRepository;
    @Autowired
    FlowerRepository flowerRepository;
    @Autowired
    EntityManager em;

    static String userId = "user";

    @BeforeEach
    void 회원넣기() {
        Flower flower = Flower.createFlower("라일락", "젊은날의 추억",  "청춘", 1, 1, "장세웅");
        flowerRepository.save(flower);

        Member member = Member.createNewMember(userId, "123", "user@kakao.com");
        memberRepository.save(member);

        FlowerLike flowerLike = FlowerLike.createFlowerLike(flower, member);
        flowerLikeRepository.save(flowerLike);

        em.flush();
        em.clear();

        System.out.println("============================== BeforeEach END =============================");
    }

    /**
     * 먼저 Member와 flowerLike는 양방향 관계이다.
     * Member를 먼저 삭제하면
     * flowerLike 에 update Query가 발생한다.
     * 그 이유는 연관관계의 주인이 flowerLike인데
     * Member가 삭제되었으니, 이를 JPA가 반영하는 것 입니다.
     *
     * 따라서 2가지 방법이 존재합니다.
     * 1. 부모(flowerLike) 엔티티를 먼저 삭제한다.
     * 2. 자식과의 연관관계를 끊은 후에, Member와 FlowerLike를 삭제한다.(CASCADE 이용)
     */

    @Test
    @DisplayName("자식(Member)보다 부모(FlowerLike)를 먼저 삭제했을 때")
    void 회원탈퇴_쿼리_확인_flowerLike에_update발생() {
        // 회원 조회
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        Member findMember = optionalMember.orElse(null);

        // 사용자가 누른 좋아요 꽃 목록
        List<FlowerLike> flowerLikes = flowerLikeRepository.findAllByMember(findMember);
        // 좋아요 감소
        for (FlowerLike flowerLike : flowerLikes) {
            flowerLike.getFlower().totalLikeLogic(false);
        }

        // 좋아요 삭제
        flowerLikeRepository.deleteAll(flowerLikes);
        // 회원 삭제
        memberRepository.delete(findMember);

        em.flush();
        em.clear();
    }

    // @Disabled
    @Test
    @DisplayName("부모(FlowerLike)보다 자식(Member)을 먼저 삭제했을 때")
    void 회원탈퇴_쿼리_확인_flowerLike에_update발생안함() {
        // 회원 조회
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        Member findMember = optionalMember.orElse(null);

        // 사용자가 누른 좋아요 꽃 목록
        List<FlowerLike> flowerLikes = flowerLikeRepository.findAllByMember(findMember);
        // 좋아요 감소
        for (FlowerLike flowerLike : flowerLikes) {
            flowerLike.getFlower().totalLikeLogic(false);
        }

        // 회원 삭제
        memberRepository.delete(findMember);
        // 좋아요 삭제
        flowerLikeRepository.deleteAll(flowerLikes);

        em.flush();
        em.clear();
    }

}