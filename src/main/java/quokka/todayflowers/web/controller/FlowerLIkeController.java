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
import quokka.todayflowers.web.response.BasicFlowerForm;
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
    private final FlowerLikeService flowerLikeService;

    // 좋아요 클릭
    @ResponseBody
    @PostMapping(value = "/like")
    public ResponseEntity<FlowerLikeResponse> flowerLike(@RequestBody FlowerLikeForm form) {
        FlowerLikeResponse flowerLikeResponse = flowerService.likeFlower(form.getFlowerId(), form.getLike());

        return new ResponseEntity<>(flowerLikeResponse, HttpStatus.OK);
    }

    // 사용자가 누른 좋아요 목록
    @GetMapping("/like-list")
    public String flowerLikeList(@PageableDefault(size = 6) Pageable pageable,
                                 Model model) {

        // 사용자가 누른 좋아요 출력
        BasicFlowerForm basicFlowerForm = flowerLikeService.findFlowerLikeListByMember(pageable);
        model.addAttribute("basicFlowerForm", basicFlowerForm);

        return "member/likeFlowerList";
    }
}
