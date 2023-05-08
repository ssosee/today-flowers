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
import quokka.todayflowers.web.response.FlowerListForm;
import quokka.todayflowers.web.response.PageDto;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/today-flower")
public class LikeRankController {

    private final FlowerRepository flowerRepository;
    private final FlowerService flowerService;
    private final SimpleCommonMethod simpleCommonMethod;

    // 좋아요 랭킹
    @GetMapping("/like-rank-list")
    public String rankList(@PageableDefault(size = 6) Pageable pageable, Model model) {
        // 좋아요 갯수 내림차순으로 조회
        Page<Flower> pageFlower = flowerRepository.findFlowerByOrderByTotalLikeDesc(pageable);
        // DTO로 변환
        List<FlowerListForm> flowerList = flowerService.getFlowerList(pageFlower.getContent());

        // 일정 범위의 페이지네이션을 보여주기 위한 변수
        int currentPage = pageFlower.getNumber();
        int totalPages = pageFlower.getTotalPages();
        // 시작 페이지 끝 페이지 계산
        PageDto pageDto = simpleCommonMethod.getPageDto(totalPages, currentPage);

        model.addAttribute("flowerList", flowerList); // 페이지에 들어갈 내용
        model.addAttribute("currentPage", currentPage); // 현재 페이지
        model.addAttribute("totalPages", pageFlower.getTotalPages()); // 전체 페이지
        model.addAttribute("startPage", pageDto.getStartPage()); // 시작 페이지
        model.addAttribute("endPage", pageDto.getEndPage()); // 끝 페이지

        return "flower/like-rank/likeRankFlowerList";
    }
}
