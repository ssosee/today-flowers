package quokka.todayflowers.domain.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import quokka.todayflowers.domain.service.response.BirthFlowerResponse;
import quokka.todayflowers.domain.service.response.TodayFlowerResponse;
import quokka.todayflowers.global.constant.ConstFlower;
import quokka.todayflowers.web.response.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@AppTest
@Transactional
class FlowerServiceTest {

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

            FlowerLike flowerLike = FlowerLike.createFlowerLike(flower, member);
            flowerLikeRepository.save(flowerLike);
        }


        System.out.println("============================== BeforeEach END =============================");
    }

    @Test
    @DisplayName("오늘의 꽃 조회")
    void findTodayFlowerTest() {
        LocalDateTime now = LocalDateTime.now();
        int day = now.getDayOfMonth();

        TodayFlowerResponse todayFlower = flowerService.findTodayFlower(userId);

        assertAll(
                () -> assertEquals(userId, todayFlower.getUserId()),
                () -> assertEquals("꽃"+day, todayFlower.getName()),
                () -> assertEquals("이쁜꽃"+day, todayFlower.getFlowerLang()),
                () -> assertEquals("꽃"+day+" 입니다.", todayFlower.getDescription())
        );
    }

    @Test
    @DisplayName("오늘의 꽃 아이디로 조회")
    void findTodayFlowerTestByFlowerId() {
        Optional<Flower> optionalFlower = flowerRepository.findFlowerByMonthAndDay(month, day);
        Flower flower = optionalFlower.get();

        TodayFlowerResponse todayFlower = flowerService.findFlowerByFlowerId(flower.getId(), userId);
        assertAll(
                () -> assertEquals(flower.getId(), todayFlower.getFlowerId()),
                () -> assertEquals(userId, todayFlower.getUserId()),
                () -> assertEquals("꽃"+day, todayFlower.getName()),
                () -> assertEquals("이쁜꽃"+day, todayFlower.getFlowerLang()),
                () -> assertEquals("꽃"+day+" 입니다.", todayFlower.getDescription())
        );
    }

    @Test
    @DisplayName("생일의 꽃 조회")
    void findBirthFlowerTest() {

        String birth = getBirth();
        BirthFlowerResponse birthFlower = flowerService.findBirthFlower(birth, userId);

        Optional<Flower> optionalFlower = flowerRepository.findFlowerByMonthAndDay(month, day);
        Flower flower = optionalFlower.get();

        assertAll(
                () -> assertEquals(flower.getId(), birthFlower.getFlowerId()),
                () -> assertEquals(flower.getFlowerLang(), birthFlower.getFlowerLang()),
                () -> assertEquals(flower.getName(), birthFlower.getName()),
                () -> assertEquals(flower.getDescription(), birthFlower.getDescription()),
                () -> assertEquals(flower.getHits(), birthFlower.getHits()),
                () -> assertEquals(flower.getTotalLike(), birthFlower.getTotalLike()),
                () -> assertEquals(flower.getFlowerPhotos().get(0).getPath(), birthFlower.getPhotoPath().get(0))
        );
    }

    // 생일 계산 포맷터
    private static String getBirth() {
        // yyMMdd 같은 포맷으로
        // e.g) 970214
        StringBuilder sb = new StringBuilder();
        sb.append("97");
        if(month / 10 == 0) {
            sb.append("0").append(month);
        } else {
            sb.append(month);
        }
        if(day / 10 == 0) {
            sb.append("0").append(day);
        } else {
            sb.append(day);
        }

        return sb.toString();
    }

    @Test
    @DisplayName("좋아요")
    void likeFlowerTest() {
        Optional<Flower> optionalFlower = flowerRepository.findFlowerByMonthAndDay(month, day);
        Flower flower = optionalFlower.get();

        // 좋아요 클릭
        FlowerLikeResponse result = flowerService.likeFlower(flower.getId(), userId, false);

        assertAll(
                () -> assertEquals(1, result.getTotalLikeCount()),
                () -> assertEquals(ConstFlower.FLOWER_FOUND, result.getMessage()),
                () -> assertTrue(result.getLike())
        );
    }

    @Test
    @DisplayName("꽃말의 꽃 조회")
    void findLangFlowerTest() {
        String lang = "이쁜꽃"+day;
        Pageable pageable = PageRequest.of(0, 6);
        // 꽃말의 꽃 조회
        BasicFlowerForm langFlower = flowerService.findLangFlower(pageable, lang);
        List<FlowerListForm> flowerList = langFlower.getFlowerList();

        // 꽃말로 꽃 조회(비교 대상)
        Page<Flower> flowerLangPage = flowerRepository.findFlowerByFlowerLangContainingOrderByFlowerLang(pageable, lang);
        List<Flower> flowerContent = flowerLangPage.getContent();

        // 크기가 같은지
        assertEquals(flowerContent.size(), flowerList.size());

        // 데이터가 일치하는지
        for(int i = 0; i < flowerList.size(); i++) {
            assertEquals(flowerContent.get(i).getId(), flowerList.get(i).getId());
            assertEquals(flowerContent.get(i).getFlowerLang(), flowerList.get(i).getLang());
            assertEquals(flowerContent.get(i).getHits(), flowerList.get(i).getHits());
            assertEquals(flowerContent.get(i).getName(), flowerList.get(i).getName());
            assertEquals(flowerContent.get(i).getTotalLike(), flowerList.get(i).getTotalLike());
        }

    }

    @Test
    @DisplayName("이름의 꽃 조회")
    void findNameFlowerTest() {
        String name = "꽃"+day;
        Pageable pageable = PageRequest.of(0, 6);
        // 이름의 꽃 조회
        BasicFlowerForm langFlower = flowerService.findNameFlower(pageable, name);
        List<FlowerListForm> flowerList = langFlower.getFlowerList();

        // 이름으로 꽃 조회(비교 대상)
        Page<Flower> flowerLangPage = flowerRepository.findFlowerByNameContainingOrderByName(pageable, name);
        List<Flower> flowerContent = flowerLangPage.getContent();

        // 크기가 같은지
        assertEquals(flowerContent.size(), flowerList.size());

        // 데이터가 일치하는지
        for(int i = 0; i < flowerList.size(); i++) {
            assertEquals(flowerContent.get(i).getId(), flowerList.get(i).getId());
            assertEquals(flowerContent.get(i).getFlowerLang(), flowerList.get(i).getLang());
            assertEquals(flowerContent.get(i).getHits(), flowerList.get(i).getHits());
            assertEquals(flowerContent.get(i).getName(), flowerList.get(i).getName());
            assertEquals(flowerContent.get(i).getTotalLike(), flowerList.get(i).getTotalLike());
        }
    }

    @Test
    @DisplayName("좋아요 랭킹 조회")
    void findLikeFlowerRank() {

        // 좋아요 랭킹 조회
        Pageable pageable = PageRequest.of(0, 6);
        BasicFlowerForm result = flowerService.findLikeFlowerRank(pageable);

        assertAll(
                () -> assertEquals(3, result.getFlowerList().size()),
                () -> assertEquals(0, result.getStartPage()),
                () -> assertEquals(0, result.getEndPage())
        );
    }
}