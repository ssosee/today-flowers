package quokka.todayflowers.domain.repository;

import groovy.util.logging.Slf4j;
import org.assertj.core.error.NoElementsShouldMatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import quokka.AppTest;
import quokka.todayflowers.domain.entity.Flower;
import quokka.todayflowers.domain.entity.FlowerLike;
import quokka.todayflowers.domain.entity.FlowerPhoto;
import quokka.todayflowers.domain.entity.Member;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@AppTest
@Transactional
class FlowerRepositoryTest {

    @Autowired
    FlowerRepository flowerRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    FlowerPhotoRepository flowerPhotoRepository;
    @Autowired
    FlowerLikeRepository flowerLikeRepository;

    final String userId = "user";
    static LocalDateTime now = LocalDateTime.now();
    static int day = now.getDayOfMonth();
    static int month = now.getMonthValue();


    @Test
    @DisplayName("파라미터로 회원 아이디, 월, 일을 받아 flower, flowerLike, flowerPhoto 테이블을 조회한다.")
    void findFlowerBy() {
        // given
        Member member = Member.createNewMember(userId, "123", "user@kakao.com");
        memberRepository.save(member);

        FlowerPhoto flowerPhoto = FlowerPhoto.createFlowerPhoto("path", "장세웅");
        flowerPhotoRepository.save(flowerPhoto);

        // 시간 조회
        LocalDateTime date = now.plusDays(0);
        int day = date.getDayOfMonth();
        int month = date.getMonthValue();

        Flower flower = Flower.createFlower("꽃"+day, "이쁜꽃"+day, "꽃"+day+" 입니다.", month, day, "장세웅");
        flowerRepository.save(flower);
        flower.changeFlowerPhoto(flowerPhoto);

        FlowerLike flowerLike = FlowerLike.createFlowerLike(flower, member);
        flowerLikeRepository.save(flowerLike);

        // when
        Flower findFlower = flowerRepository.findFlowerBy(month, day, userId).get();

        // then
        assertThat(findFlower).isNotNull();
    }

    @Test
    @DisplayName("회원이 좋아요를 누른 꽃이 아닐때 파라미터로 회원 아이디, 월, 일을 받아 flower, flowerLike, flowerPhoto 테이블을 조회하면 예외가 발생한다.")
    void NoData_findFlowerBy() {
        // given
        Member member = Member.createNewMember(userId, "123", "user@kakao.com");
        memberRepository.save(member);

        FlowerPhoto flowerPhoto = FlowerPhoto.createFlowerPhoto("path", "장세웅");
        flowerPhotoRepository.save(flowerPhoto);

        // 시간 조회
        LocalDateTime date = now.plusDays(0);
        int day = date.getDayOfMonth();
        int month = date.getMonthValue();

        Flower flower = Flower.createFlower("꽃"+day, "이쁜꽃"+day, "꽃"+day+" 입니다.", month, day, "장세웅");
        flowerRepository.save(flower);
        flower.changeFlowerPhoto(flowerPhoto);

        // when
        Flower findFlower = flowerRepository.findFlowerBy(month, day, userId).orElse(null);

        // then
        assertThat(findFlower).isNull();
    }
}