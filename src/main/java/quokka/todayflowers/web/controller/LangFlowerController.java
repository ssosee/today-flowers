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
import quokka.todayflowers.web.request.LangFlowerForm;
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
                                 @PageableDefault(size = 6) Pageable pageable,
                                 Model model) {

        Page<Flower> pageFlower;
        if(lang == null) {
            // 전체 꽃 조회
            pageFlower = flowerRepository.findFlowerByOrderByFlowerLang(pageable);
        } else {
            pageFlower = flowerRepository.findFlowerByFlowerLangContainingOrderByFlowerLang(pageable, lang);
            model.addAttribute("lang", lang);
        }

        // DTO 변환
        List<FlowerListForm> flowerList = flowerService.getFlowerList(pageFlower.getContent());

        // 일정 범위의 페이지네이션을 보여주기 위한 변수
        int currentPage = pageFlower.getNumber();
        int totalPages = pageFlower.getTotalPages();
        // 시작 페이지 끝 페이지 계산
        PageDto pageDto = simpleCommonMethod.getPageDto(totalPages, currentPage);

        model.addAttribute("flowerList", flowerList); // 페이지에 들어갈 내용
        model.addAttribute("currentPage", currentPage); // 현재 페이지
        model.addAttribute("totalPages", pageFlower.getTotalPages()); // 전체 페이지
        model.addAttribute("startPage", pageDto.getStartPage()); // 시작 페이지
        model.addAttribute("endPage", pageDto.getEndPage()); // 끝 페이지

        return "flower/lang/langFlowerList";
    }

    // 꽃말로 꽃 조회
    @PostMapping("/lang-list")
    public String findLangFlowerList(@Validated @ModelAttribute("form") LangFlowerForm form,
                                     BindingResult bindingResult,
                                     @PageableDefault(size = 6) Pageable pageable) {
        // 꽃말로 꽃 조회
        Page<Flower> pageFlower = flowerRepository.findFlowerByFlowerLangContainingOrderByFlowerLang(pageable, form.getLang());

        // 꽃말을 가진 꽃이 없으면
        if(pageFlower.getContent().isEmpty()) {
            bindingResult.reject("lang_flower_not_found", "'"+form.getLang() + ConstFlower.LANG_FLOWER_NOT_FOUND);
            return "flower/lang/langFlowerList";
        }

        String encode = URLEncoder.encode(form.getLang(), StandardCharsets.UTF_8);
        return "redirect:/today-flower/lang-list?page=0&lang="+encode;
    }
}
