package quokka.todayflowers.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import quokka.todayflowers.domain.service.FlowerService;
import quokka.todayflowers.web.request.LangFlowerForm;
import quokka.todayflowers.web.response.FlowerListForm;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/today-flower")
public class LangFlowerController {

    private final FlowerService flowerService;

    @GetMapping("/lang-list")
    public String langFlowerList(@ModelAttribute("form") LangFlowerForm form,
                                 @PageableDefault(size = 5) Pageable pageable,
                                 Model model) {

        List<FlowerListForm> flowerList = flowerService.findFlowerList(pageable);
        model.addAttribute("flowerList", flowerList);

        return "/flower/lang/flowerList";
    }

    @PostMapping("/lang-list")
    public String findLangFlowerList(@Validated @ModelAttribute("form") LangFlowerForm form,
                                     BindingResult bindingResult,
                                     @PageableDefault(size = 5) Pageable pageable,
                                     Model model) {

        List<FlowerListForm> flowerList = flowerService.findLangFlowerList(pageable, form.getLang());
        model.addAttribute("flowerList", flowerList);

        return "/flower/lang/flowerList";
    }
}
