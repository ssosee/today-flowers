package quokka.todayflowers.domain.service;

import quokka.todayflowers.domain.entity.FlowerLike;
import quokka.todayflowers.web.response.FlowerListForm;

import java.util.List;

public interface FlowerLikeService {
    // DTO로 변환
    List<FlowerListForm> getFlowerListForm(List<FlowerLike> flowerLikeList);
}
