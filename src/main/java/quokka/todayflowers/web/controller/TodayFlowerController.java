package quokka.todayflowers.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import quokka.todayflowers.web.response.TodayFlowerForm;

@Controller
@RequiredArgsConstructor
@RequestMapping("/today-flower")
public class TodayFlowerController {


    @GetMapping("/today")
    public String todayFlower(@ModelAttribute("form") TodayFlowerForm form) {

        return "/flower/today/todayFlower";
    }
}
