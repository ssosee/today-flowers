package quokka.todayflowers.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import quokka.todayflowers.domain.entity.Flower;
import quokka.todayflowers.domain.repository.FlowerRepository;
import quokka.todayflowers.domain.service.FlowerService;
import quokka.todayflowers.global.common.SimpleCommonMethod;
import quokka.todayflowers.global.constant.ConstFlower;
import quokka.todayflowers.global.exception.LangException;
import quokka.todayflowers.web.request.LangFlowerForm;
import quokka.todayflowers.web.response.BasicFlowerForm;
import quokka.todayflowers.web.response.FlowerListForm;
import quokka.todayflowers.web.response.PageDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/today-flower")
public class LangFlowerController {

    private final FlowerService flowerService;
    private final FlowerRepository flowerRepository;
    private final SimpleCommonMethod simpleCommonMethod;

    // 꽃말의 꽃
    @GetMapping("/lang-list")
    public String langFlowerList(@ModelAttribute("form") LangFlowerForm form,
                                 @RequestParam(value = "lang", required = false) String lang,
                                 @RequestParam(value = "error", required = false) String error,
                                 @RequestParam(value = "exception", required = false) String exception,
                                 @PageableDefault(size = 6) Pageable pageable,
                                 Model model) {

        // 꽃말의 꽃 조회
        BasicFlowerForm basicFlowerForm = flowerService.findLangFlower(pageable, lang);

        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        model.addAttribute("basicFlowerForm", basicFlowerForm);

        return "flower/lang/langFlowerList";
    }

    // 꽃말로 꽃 조회
    @PostMapping("/lang-list")
    public String findLangFlowerList(@Validated @ModelAttribute("form") LangFlowerForm form,
                                     BindingResult bindingResult,
                                     @PageableDefault(size = 6) Pageable pageable,
                                     Model model) {

        // 요청 데이터 검증
        if(bindingResult.hasErrors()) {
            model.addAttribute("error", false);
            model.addAttribute("exception", null);
            return "flower/lang/langFlowerList";
        }

        // 꽃말의 꽃 조회
        BasicFlowerForm basicFlowerForm = flowerService.findLangFlower(pageable, form.getLang());

        model.addAttribute("basicFlowerForm", basicFlowerForm);

        return "flower/lang/langFlowerList";
    }
}
