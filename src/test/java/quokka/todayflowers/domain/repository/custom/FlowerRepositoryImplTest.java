package quokka.todayflowers.domain.repository.custom;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@AppTest
@Transactional
class FlowerRepositoryImplTest {
    @Autowired
    FlowerRepository flowerRepository;
    @Autowired
    FlowerRepositoryImpl flowerRepositoryImpl;
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
        Flower findFlower = flowerRepositoryImpl.findFlowerBy(month, day, userId).get();

        // then
        assertThat(findFlower).isNotNull();
    }

    @Test
    @DisplayName("회원이 좋아요를 누른 꽃이 아닐때 파라미터로 회원 아이디, 월, 일을 받아 flower, flowerLike, flowerPhoto 테이블을 조회하면 NULL이 반환된다.")
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
        Flower findFlower = flowerRepositoryImpl.findFlowerBy(month, day, userId).orElse(null);

        // then
        assertThat(findFlower).isNull();
    }

    @Test
    @DisplayName("파라미터로 월, 일을 받아 flower, flowerLike, flowerPhoto 테이블을 조회하면 예외가 발생한다.")
    void NoUserId_findFlowerBy() {
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
        Flower findFlower = flowerRepositoryImpl.findFlowerBy(month, day, "").orElse(null);

        // then
        assertThat(findFlower).isNotNull();
    }
}