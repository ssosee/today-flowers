package quokka.todayflowers.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import quokka.todayflowers.domain.entity.Flower;
import quokka.todayflowers.domain.repository.FlowerRepository;
import quokka.todayflowers.domain.service.FlowerService;
import quokka.todayflowers.global.common.SimpleCommonMethod;
import quokka.todayflowers.web.response.BasicFlowerForm;
import quokka.todayflowers.web.response.FlowerListForm;
import quokka.todayflowers.web.response.PageDto;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/today-flower")
public class LikeRankController {

    private final FlowerService flowerService;

    // 좋아요 랭킹
    @GetMapping("/like-rank-list")
    public String rankList(@PageableDefault(size = 6) Pageable pageable, Model model) {

        // 좋아요 랭킹 조회
        BasicFlowerForm basicFlowerForm = flowerService.findLikeFlowerRank(pageable);
        model.addAttribute("basicFlowerForm", basicFlowerForm);

        return "flower/like-rank/likeRankFlowerList";
    }
}
