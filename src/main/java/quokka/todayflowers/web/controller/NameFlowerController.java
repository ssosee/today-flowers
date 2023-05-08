package quokka.todayflowers.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import quokka.todayflowers.domain.entity.Flower;
import quokka.todayflowers.domain.repository.FlowerRepository;
import quokka.todayflowers.domain.service.FlowerService;
import quokka.todayflowers.global.common.SimpleCommonMethod;
import quokka.todayflowers.global.constant.ConstFlower;
import quokka.todayflowers.web.request.NameFlowerForm;
import quokka.todayflowers.web.response.FlowerListForm;
import quokka.todayflowers.web.response.PageDto;

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
                                 @PageableDefault(size = 6) Pageable pageable,
                                 Model model) {

        // 꽃 리스트 조회(이름 내림차순)
        Page<Flower> pageFlower = flowerRepository.findFlowerByOrderByName(pageable);
        // DTO로 변환
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

        return "flower/name/nameFlowerList";

    }

    // 꽃 이름으로 조회
    @PostMapping("/name-list")
    public String findNameFlowerList(@Validated @ModelAttribute("form") NameFlowerForm form,
                                     BindingResult bindingResult,
                                     @PageableDefault(size = 6) Pageable pageable,
                                     Model model) {

        // 꽃 리스트 이름으로 조회
        Page<Flower> pageFlower = flowerRepository.findFlowerByNameContainingOrderByName(pageable, form.getName());
        // DTO로 변환
        List<FlowerListForm> flowerList = flowerService.getFlowerList(pageFlower.getContent());

        if(flowerList.isEmpty()) {
            bindingResult.reject("name_flower_not_found", "'" + form.getName() + ConstFlower.NAME_FLOWER_NOT_FOUND);
            return "flower/name/nameFlowerList";
        }

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


        return "flower/name/nameFlowerList";
    }
}
