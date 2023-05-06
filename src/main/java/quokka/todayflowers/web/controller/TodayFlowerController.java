package quokka.todayflowers.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import quokka.todayflowers.domain.service.FlowerService;
import quokka.todayflowers.global.constant.ConstFlower;
import quokka.todayflowers.web.request.FlowerLikeForm;
import quokka.todayflowers.web.response.TodayFlowerForm;

@Controller
@RequiredArgsConstructor
@RequestMapping("/today-flower")
public class TodayFlowerController {

    private final FlowerService flowerService;

    // 오늘의 꽃
    @GetMapping(value = {"/today", "/today/{flower_id}"})
    public String todayFlower(@PathVariable(value = "flower_id", required = false) Long flowerId,
                              Model model) {

        // 아이디가 path에 있을 경우
        if(flowerId != null) {
            TodayFlowerForm todayFlower = flowerService.findFlower(flowerId);

            // 아이디에 맞는 꽃이 없는 경우
            if(todayFlower == null) {
                model.addAttribute("error", ConstFlower.FLOWER_NOT_FOUND);
                return "/flower/today/todayFlower";
            }

            model.addAttribute("form", todayFlower);

            return "/flower/today/todayFlower";
        }

        // 오늘의 꽃 조회
        TodayFlowerForm todayFlower = flowerService.findTodayFlower();
        model.addAttribute("form", todayFlower);

        return "/flower/today/todayFlower";
    }
}
