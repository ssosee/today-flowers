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
import quokka.todayflowers.web.request.LangFlowerForm;
import quokka.todayflowers.web.response.FlowerListForm;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/today-flower")
public class LangFlowerController {

    private final FlowerService flowerService;
    private final FlowerRepository flowerRepository;

    // 꽃말의 꽃
    @GetMapping("/lang-list")
    public String langFlowerList(@ModelAttribute("form") LangFlowerForm form,
                                 @PageableDefault(size = 6) Pageable pageable,
                                 Model model) {

        // 전체 꽃 조회
        Page<Flower> pageFlower = flowerRepository.findFlowerByOrderByFlowerLang(pageable);
        // DTO 변환
        List<FlowerListForm> flowerList = flowerService.getFlowerList(pageFlower.getContent());

        model.addAttribute("flowerList", flowerList); // 페이지에 들어갈 내용
        model.addAttribute("currentPage", pageFlower.getNumber()); // 현재 페이지
        model.addAttribute("totalPages", pageFlower.getTotalPages()); // 전체 페이지

        return "/flower/lang/langFlowerList";
    }

    // 꽃말로 꽃 조회
    @PostMapping("/lang-list")
    public String findLangFlowerList(@Validated @ModelAttribute("form") LangFlowerForm form,
                                     BindingResult bindingResult,
                                     @PageableDefault(size = 6) Pageable pageable,
                                     Model model) {
        // 꽃말로 꽃 조회
        Page<Flower> pageFlower = flowerRepository.findFlowerByFlowerLangContainingOrderByFlowerLang(pageable, form.getLang());
        // DTO로 변환
        List<FlowerListForm> flowerList = flowerService.getFlowerList(pageFlower.getContent());

        model.addAttribute("flowerList", flowerList); // 페이지에 들어갈 내용
        model.addAttribute("currentPage", pageFlower.getNumber()); // 현재 페이지
        model.addAttribute("totalPages", pageFlower.getTotalPages()); // 전체 페이지

        return "/flower/lang/langFlowerList";
    }
}
