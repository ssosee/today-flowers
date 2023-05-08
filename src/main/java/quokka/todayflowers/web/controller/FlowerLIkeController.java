package quokka.todayflowers.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import quokka.todayflowers.domain.entity.FlowerLike;
import quokka.todayflowers.domain.entity.Member;
import quokka.todayflowers.domain.repository.FlowerLikeRepository;
import quokka.todayflowers.domain.service.FlowerLikeService;
import quokka.todayflowers.domain.service.FlowerService;
import quokka.todayflowers.domain.service.MemberService;
import quokka.todayflowers.global.common.SimpleCommonMethod;
import quokka.todayflowers.global.constant.ConstFlower;
import quokka.todayflowers.web.request.FlowerLikeForm;
import quokka.todayflowers.web.response.FlowerLikeResponse;
import quokka.todayflowers.web.response.FlowerListForm;
import quokka.todayflowers.web.response.PageDto;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/flower")
public class FlowerLIkeController {

    private final FlowerService flowerService;
    private final FlowerLikeRepository flowerLikeRepository;
    private final FlowerLikeService flowerLikeService;
    private final SimpleCommonMethod simpleCommonMethod;

    // 좋아요 클릭
    @ResponseBody
    @PostMapping(value = "/like")
    public ResponseEntity<FlowerLikeResponse> flowerLike(@RequestBody FlowerLikeForm form) {
        FlowerLikeResponse flowerLikeResponse = flowerService.likeFlower(form.getFlowerId(), form.getLike());

        return new ResponseEntity<>(flowerLikeResponse, HttpStatus.OK);
    }

    // 사용자가 누른 좋아요 목록
    @GetMapping("/like-list")
    public String flowerLikeList(@PageableDefault(size = 6) Pageable pageable, Model model) {

        String userId = simpleCommonMethod.getCurrentUserId();

        Page<FlowerLike> pageFlowerLike = flowerLikeRepository.findByUserId(pageable, userId);
        List<FlowerListForm> flowerList = flowerLikeService.getFlowerListForm(pageFlowerLike.getContent());

        if(flowerList.isEmpty()) {
            model.addAttribute("error", "좋아요를 누른 꽃이 없습니다!");
            return "member/likeFlowerList";
        }

        // 일정 범위의 페이지네이션을 보여주기 위한 변수
        int currentPage = pageFlowerLike.getNumber();
        int totalPages = pageFlowerLike.getTotalPages();
        // 시작 페이지 끝 페이지 계산
        PageDto pageDto = simpleCommonMethod.getPageDto(totalPages, currentPage);

        model.addAttribute("flowerList", flowerList); // 페이지에 들어갈 내용
        model.addAttribute("currentPage", currentPage); // 현재 페이지
        model.addAttribute("totalPages", pageFlowerLike.getTotalPages()); // 전체 페이지
        model.addAttribute("startPage", pageDto.getStartPage()); // 시작 페이지
        model.addAttribute("endPage", pageDto.getEndPage()); // 끝 페이지

        return "member/likeFlowerList";
    }
}
