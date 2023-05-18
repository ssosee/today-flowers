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
    public String birthFlower(@ModelAttribute("form") BirthFlowerForm form,
                              @RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "exception", required = false) String exception,
                              Model model) {

        // 에러 정보를 model에 저장
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "flower/birth/birthFlower";
    }

    @PostMapping("/birth")
    public String findBirthFlower(@Validated @ModelAttribute("form") BirthFlowerForm form,
                                  BindingResult bindingResult,
                                  Model model) {
        // 요청 데이터 검증
        if(bindingResult.hasErrors()) {
            model.addAttribute("error", false);
            model.addAttribute("exception", null);
            return "flower/birth/birthFlower";
        }

        // 스프링시큐리티 컨테스트에서 userId 꺼내기
        String userId = simpleCommonMethod.getCurrentUserId();

        // 생일의 꽃 조회
        BirthFlowerForm birthFlower = flowerService.findBirthFlower(form.getBirth(), userId);

        model.addAttribute("form", birthFlower);

        return "flower/birth/birthFlower";
    }
}
