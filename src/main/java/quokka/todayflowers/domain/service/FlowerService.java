package quokka.todayflowers.domain.service;

import org.springframework.data.domain.Pageable;
import quokka.todayflowers.domain.entity.Flower;
import quokka.todayflowers.web.request.LangFlowerForm;
import quokka.todayflowers.web.response.BirthFlowerForm;
import quokka.todayflowers.web.response.FlowerLikeResponse;
import quokka.todayflowers.web.response.FlowerListForm;
import quokka.todayflowers.web.response.TodayFlowerForm;

import java.util.List;

public interface FlowerService {
    // 오늘의 꽃 조회
    TodayFlowerForm findTodayFlower();
    // 오늘의 꽃 아이디로 조회
    TodayFlowerForm findFlower(Long flowerId);
    // 생일의 꽃 조회
    BirthFlowerForm findBirthFlower(String birth);
    // 꽃 리스트 DTO로 변환
    List<FlowerListForm> getFlowerList(List<Flower> flowerList);
    // 좋아요
    FlowerLikeResponse likeFlower(Long flowerId, Boolean like);
}
