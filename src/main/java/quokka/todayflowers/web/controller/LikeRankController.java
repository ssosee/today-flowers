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
import quokka.todayflowers.web.response.FlowerListForm;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/today-flower")
public class LikeRankController {

    private final FlowerRepository flowerRepository;
    private final FlowerService flowerService;

    // 좋아요 랭킹
    @GetMapping("/like-rank-list")
    public String rankList(@PageableDefault(size = 6) Pageable pageable, Model model) {
        // 좋아요 갯수 내림차순으로 조회
        Page<Flower> pageFlower = flowerRepository.findFlowerByOrderByTotalLikeDesc(pageable);
        // DTO로 변환
        List<FlowerListForm> flowerList = flowerService.getFlowerList(pageFlower.getContent());

        model.addAttribute("flowerList", flowerList);
        model.addAttribute("currentPage", pageFlower.getNumber());
        model.addAttribute("totalPages", pageFlower.getTotalPages());

        return "flower/like-rank/likeRankFlowerList";
    }
}
