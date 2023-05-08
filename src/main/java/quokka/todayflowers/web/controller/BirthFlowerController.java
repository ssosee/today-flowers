package quokka.todayflowers.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import quokka.todayflowers.domain.service.FlowerService;
import quokka.todayflowers.global.common.SimpleCommonMethod;
import quokka.todayflowers.global.constant.ConstFlower;
import quokka.todayflowers.web.response.BirthFlowerForm;
import quokka.todayflowers.web.response.TodayFlowerForm;

@Controller
@RequiredArgsConstructor
@RequestMapping("/today-flower")
public class BirthFlowerController {

    private final FlowerService flowerService;
    private final SimpleCommonMethod simpleCommonMethod;

    // 생일의 꽃
    @GetMapping("/birth")
    public String birthFlower(@ModelAttribute("form") BirthFlowerForm form) {

        return "flower/birth/birthFlower";
    }

    @PostMapping("/birth")
    public String findBirthFlower(@Validated @ModelAttribute("form") BirthFlowerForm form,
                                  BindingResult bindingResult,
                                  Model model) {
        // 요청 데이터 검증
        if(bindingResult.hasErrors()) {
            return "flower/birth/birthFlower";
        }

        // 생일의 꽃 조회
        BirthFlowerForm birthFlower = flowerService.findBirthFlower(form.getBirth());

        // 생일의 꽃이 없으면
        if(birthFlower == null) {
            bindingResult.reject("birth_flower_not_found", ConstFlower.BIRTH_FLOWER_NOT_FOUND);
            return "flower/birth/birthFlower";
        }

        model.addAttribute("form", birthFlower);

        return "flower/birth/birthFlower";
    }
}
