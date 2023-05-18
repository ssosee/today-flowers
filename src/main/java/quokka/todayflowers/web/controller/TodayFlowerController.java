package quokka.todayflowers.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import quokka.todayflowers.domain.service.FlowerService;
import quokka.todayflowers.global.common.SimpleCommonMethod;
import quokka.todayflowers.global.constant.ConstFlower;
import quokka.todayflowers.web.request.FlowerLikeForm;
import quokka.todayflowers.web.response.TodayFlowerForm;

@Controller
@RequiredArgsConstructor
@RequestMapping("/today-flower")
public class TodayFlowerController {

    private final FlowerService flowerService;
    private final SimpleCommonMethod simpleCommonMethod;

    // 오늘의 꽃
    @GetMapping(value = {"/today", "/today/{flower_id}"})
    public String todayFlower(@PathVariable(value = "flower_id", required = false) Long flowerId,
                              @RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "exception", required = false) String exception,
                              Model model) {

        // 스프링시큐리티 컨테스트에서 userId 꺼내기
        String userId = simpleCommonMethod.getCurrentUserId();
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        // 아이디가 PathVariable에 있을 경우
        if(flowerId != null) {
            TodayFlowerForm todayFlower = flowerService.findFlowerByFlowerId(flowerId, userId);
            model.addAttribute("form", todayFlower);

            return "flower/today/todayFlower";
        }

        // 오늘의 꽃 조회
        TodayFlowerForm todayFlower = flowerService.findTodayFlower(userId);
        model.addAttribute("form", todayFlower);

        return "flower/today/todayFlower";
    }
}
