package quokka.todayflowers.domain.service;

import quokka.todayflowers.web.response.TodayFlowerForm;

public interface FlowerService {
    // 오늘의 꽃 조회
    TodayFlowerForm findTodayFlower(String month, String day);
}
