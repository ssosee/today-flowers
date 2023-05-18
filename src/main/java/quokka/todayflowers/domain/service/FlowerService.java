package quokka.todayflowers.domain.service;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import quokka.todayflowers.domain.entity.Flower;
import quokka.todayflowers.web.response.*;

import java.util.List;

public interface FlowerService {
    // 꽃 리스트 DTO로 변환
    // List<FlowerListForm> getFlowerList(List<Flower> flowerList);
    // 오늘의 꽃 조회
    TodayFlowerForm findTodayFlower(String userId);
    // 오늘의 꽃 아이디로 조회
    TodayFlowerForm findFlowerByFlowerId(Long flowerId, String userId);
    // 생일의 꽃 조회
    BirthFlowerForm findBirthFlower(String birth, String userId);
    // 좋아요
    FlowerLikeResponse likeFlower(Long flowerId, String userId, Boolean like);
    // 꽃말의 꽃 조회
    BasicFlowerForm findLangFlower(Pageable pageable, String lang);
    // 이름의 꽃 조회
    BasicFlowerForm findNameFlower(Pageable pageable, String name);
    // 좋아요 랭킹 조회
    BasicFlowerForm findLikeFlowerRank(Pageable pageable);

}
