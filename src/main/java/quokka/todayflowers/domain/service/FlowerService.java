package quokka.todayflowers.domain.service;

import org.springframework.data.domain.Pageable;
import quokka.todayflowers.domain.service.response.BirthFlowerResponse;
import quokka.todayflowers.domain.service.response.TodayFlowerResponse;
import quokka.todayflowers.web.response.*;

public interface FlowerService {
    // 꽃 리스트 DTO로 변환
    // List<FlowerListForm> getFlowerList(List<Flower> flowerList);
    // 오늘의 꽃 조회
    TodayFlowerResponse findTodayFlower(String userId);
    // 오늘의 꽃 아이디로 조회
    TodayFlowerResponse findFlowerByFlowerId(Long flowerId, String userId);
    // 생일의 꽃 조회
    BirthFlowerResponse findBirthFlower(String birth, String userId);
    // 좋아요
    FlowerLikeResponse likeFlower(Long flowerId, String userId, Boolean like);
    // 꽃말의 꽃 조회
    BasicFlowerForm findLangFlower(Pageable pageable, String lang);
    // 이름의 꽃 조회
    BasicFlowerForm findNameFlower(Pageable pageable, String name);
    // 좋아요 랭킹 조회
    BasicFlowerForm findLikeFlowerRank(Pageable pageable);

}
