package quokka.todayflowers.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import quokka.todayflowers.domain.service.FlowerService;
import quokka.todayflowers.global.common.SimpleCommonMethod;
import quokka.todayflowers.domain.service.response.TodayFlowerResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("/today-flower")
public class TodayFlowerController {

    private final FlowerService flowerService;
    private final SimpleCommonMethod simpleCommonMethod;

    // 오늘의 꽃
    @GetMapping("/today")
    public String todayFlower(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "exception", required = false) String exception,
                              Model model) {

        // 스프링시큐리티 컨테스트에서 userId 꺼내기
        String userId = simpleCommonMethod.getCurrentUserId();

        // 오늘의 꽃 조회
        TodayFlowerResponse response = flowerService.findTodayFlower(userId);

        model.addAttribute("form", response);
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "flower/today/todayFlower";
    }

    // 꽃 상세 조회
    @GetMapping("/today/{flower_id}")
    public String todayFlowerByFlowerId(@PathVariable(value = "flower_id") Long flowerId,
                                        @RequestParam(value = "error", required = false) String error,
                                        @RequestParam(value = "exception", required = false) String exception,
                                        Model model) {

        // 스프링시큐리티 컨테스트에서 userId 꺼내기
        String userId = simpleCommonMethod.getCurrentUserId();

        // 아이디가 PathVariable에 있을 경우
        TodayFlowerResponse response = flowerService.findFlowerByFlowerId(flowerId, userId);

        model.addAttribute("form", response);
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "flower/today/todayFlower";
    }
}
