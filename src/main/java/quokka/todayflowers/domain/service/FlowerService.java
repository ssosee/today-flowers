package quokka.todayflowers.domain.service;

import quokka.todayflowers.web.response.BirthFlowerForm;
import quokka.todayflowers.web.response.FlowerLikeResponse;
import quokka.todayflowers.web.response.TodayFlowerForm;

public interface FlowerService {
    // 오늘의 꽃 조회
    TodayFlowerForm findTodayFlower();
    // 생일의 꽃 조회
    BirthFlowerForm findBirthFlower(String birth);
    // 좋아요
    FlowerLikeResponse likeFlower(Long flowerId, Boolean like);
}
