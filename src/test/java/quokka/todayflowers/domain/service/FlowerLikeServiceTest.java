package quokka.todayflowers.domain.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import quokka.AppTest;
import quokka.todayflowers.domain.entity.Flower;
import quokka.todayflowers.domain.entity.FlowerLike;
import quokka.todayflowers.domain.entity.FlowerPhoto;
import quokka.todayflowers.domain.entity.Member;
import quokka.todayflowers.domain.repository.FlowerLikeRepository;
import quokka.todayflowers.domain.repository.FlowerPhotoRepository;
import quokka.todayflowers.domain.repository.FlowerRepository;
import quokka.todayflowers.domain.repository.MemberRepository;
import quokka.todayflowers.web.response.BasicFlowerForm;
import quokka.todayflowers.web.response.FlowerLikeResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AppTest
@Transactional
class FlowerLikeServiceTest {

    @Autowired
    FlowerLikeService flowerLikeService;
    @Autowired
    FlowerService flowerService;
    @Autowired
    FlowerRepository flowerRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    FlowerLikeRepository flowerLikeRepository;
    @Autowired
    FlowerPhotoRepository flowerPhotoRepository;

    @Autowired
    EntityManager em;

    final String userId = "user";
    static LocalDateTime now = LocalDateTime.now();
    static int day = now.getDayOfMonth();
    static int month = now.getMonthValue();

    @BeforeEach
    @DisplayName("초기 데이터 설정(생성되는 모든 꽃에 좋아요 누름)")
    void 초기데이터설정() {
        Member member = Member.createNewMember(userId, "123", "user@kakao.com");
        memberRepository.save(member);

        for(int i = 0; i < 3; i++) {
            FlowerPhoto flowerPhoto = FlowerPhoto.createFlowerPhoto("path"+i, "장세웅");
            flowerPhotoRepository.save(flowerPhoto);

            // 시간 조회
            LocalDateTime date = now.plusDays(i);
            int day = date.getDayOfMonth();
            int month = date.getMonthValue();

            Flower flower = Flower.createFlower("꽃"+day, "이쁜꽃"+day, "꽃"+day+" 입니다.", month, day, "장세웅");
            flowerRepository.save(flower);
            flower.changeFlowerPhoto(flowerPhoto);

            // 좋아요 클릭
            flowerService.likeFlower(flower.getId(), userId, false);
        }


        System.out.println("============================== BeforeEach END =============================");
    }

    @Test
    @DisplayName("회원이 좋아요 누른 꽃 목록 조회")
    void findFlowerLikeListByMember() {

        // 꽃 조회
        List<Flower> flowers = flowerRepository.findAll();

        Pageable pageable = PageRequest.of(0, 6);
        BasicFlowerForm result = flowerLikeService.findFlowerLikeListByMember(pageable, userId);

        // 크기가 같은지
        assertEquals(flowers.size(), result.getFlowerList().size());

        // 데이터 비교
        for(int i = 0; i < flowers.size(); i++) {
            assertEquals(flowers.get(i).getId(), result.getFlowerList().get(i).getId());
            assertEquals(flowers.get(i).getTotalLike(), result.getFlowerList().get(i).getTotalLike());
            assertEquals(flowers.get(i).getHits(), result.getFlowerList().get(i).getHits());
            assertEquals(flowers.get(i).getName(), result.getFlowerList().get(i).getName());
            assertEquals(flowers.get(i).getFlowerLang(), result.getFlowerList().get(i).getLang());
            assertEquals(flowers.get(i).getFlowerPhotos().get(0).getPath(), result.getFlowerList().get(i).getPath());
        }
    }
}