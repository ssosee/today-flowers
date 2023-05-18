package quokka.todayflowers.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import quokka.todayflowers.domain.entity.Flower;
import quokka.todayflowers.domain.repository.FlowerRepository;
import quokka.todayflowers.domain.service.FlowerService;
import quokka.todayflowers.global.common.SimpleCommonMethod;
import quokka.todayflowers.global.constant.ConstFlower;
import quokka.todayflowers.web.request.NameFlowerForm;
import quokka.todayflowers.web.response.BasicFlowerForm;
import quokka.todayflowers.web.response.FlowerListForm;
import quokka.todayflowers.web.response.PageDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/today-flower")
public class NameFlowerController {

    private final FlowerRepository flowerRepository;
    private final FlowerService flowerService;
    private final SimpleCommonMethod simpleCommonMethod;

    // 이름의 꽃 조회
    @GetMapping("/name-list")
    public String nameFlowerList(@ModelAttribute("form") NameFlowerForm form,
                                 @RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "error", required = false) String error,
                                 @RequestParam(value = "exception", required = false) String exception,
                                 @PageableDefault(size = 6) Pageable pageable,
                                 Model model) {

        // 꽃말의 꽃 조회
        BasicFlowerForm basicFlowerForm = flowerService.findNameFlower(pageable, name);

        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        model.addAttribute("basicFlowerForm", basicFlowerForm);

        return "flower/name/nameFlowerList";

    }

    // 꽃 이름으로 조회
    @PostMapping("/name-list")
    public String findNameFlowerList(@Validated @ModelAttribute("form") NameFlowerForm form,
                                     BindingResult bindingResult,
                                     @PageableDefault(size = 6) Pageable pageable,
                                     Model model) {

        // 요청 데이터 검증
        if(bindingResult.hasErrors()) {
            model.addAttribute("error", false);
            model.addAttribute("exception", null);
            return "flower/name/nameFlowerList";
        }

        // 꽃말의 꽃 조회
        BasicFlowerForm basicFlowerForm = flowerService.findNameFlower(pageable, form.getName());

        model.addAttribute("basicFlowerForm", basicFlowerForm);
        return "flower/name/nameFlowerList";
    }
}
