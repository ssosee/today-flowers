package quokka.todayflowers.domain.service;

import org.springframework.data.domain.Pageable;
import quokka.todayflowers.web.request.LangFlowerForm;
import quokka.todayflowers.web.response.BirthFlowerForm;
import quokka.todayflowers.web.response.FlowerLikeResponse;
import quokka.todayflowers.web.response.FlowerListForm;
import quokka.todayflowers.web.response.TodayFlowerForm;

import java.util.List;

public interface FlowerService {
    // 오늘의 꽃 조회
    TodayFlowerForm findTodayFlower();
    // 생일의 꽃 조회
    BirthFlowerForm findBirthFlower(String birth);
    // 꽃말의 꽃 조회

    // 꽃 리스트 조회
    List<FlowerListForm> findFlowerList(Pageable pageable);
    // 꽃말로 꽃 리스트 조회
    List<FlowerListForm> findLangFlowerList(Pageable pageable, String lang);

    // 좋아요
    FlowerLikeResponse likeFlower(Long flowerId, Boolean like);
}
