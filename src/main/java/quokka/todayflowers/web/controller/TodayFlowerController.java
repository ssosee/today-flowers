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
    @GetMapping("/today")
    public String todayFlower(Model model) {
        // 오늘의 꽃 조회
        TodayFlowerForm todayFlower = flowerService.findTodayFlower();
        model.addAttribute("form", todayFlower);

        return "/flower/today/todayFlower";
    }
}
