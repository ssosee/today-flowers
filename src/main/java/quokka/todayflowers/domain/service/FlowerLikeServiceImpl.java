package quokka.todayflowers.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quokka.todayflowers.domain.entity.Flower;
import quokka.todayflowers.domain.entity.FlowerLike;
import quokka.todayflowers.domain.repository.FlowerLikeRepository;
import quokka.todayflowers.global.common.SimpleCommonMethod;
import quokka.todayflowers.global.constant.ConstFlowerLike;
import quokka.todayflowers.global.exception.FlowerLikeException;
import quokka.todayflowers.web.response.BasicFlowerForm;
import quokka.todayflowers.web.response.FlowerListForm;
import quokka.todayflowers.web.response.PageDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FlowerLikeServiceImpl implements FlowerLikeService {

    private final FlowerLikeRepository flowerLikeRepository;
    private final SimpleCommonMethod simpleCommonMethod;

    // DTO로 변환
    private static List<FlowerListForm> getFlowerListForm(List<FlowerLike> flowerLikeList) {

       return flowerLikeList.stream()
                .map(flowerLike -> FlowerListForm.builder()
                        .id(flowerLike.getFlower().getId())
                        .hits(flowerLike.getFlower().getHits())
                        .name(flowerLike.getFlower().getName())
                        .lang(flowerLike.getFlower().getFlowerLang())
                        .path(flowerLike.getFlower().getFlowerPhotos().get(0).getPath())
                        .totalLike(flowerLike.getFlower().getTotalLike())
                        .build())
                .collect(Collectors.toList());
    }

    // 좋아요 누른 꽃 조회
    @Override
    public BasicFlowerForm findFlowerLikeListByMember(Pageable pageable, String userId) {

        Page<FlowerLike> pageFlowerLike = flowerLikeRepository.findByUserId(pageable, userId);
        List<FlowerListForm> flowerList = getFlowerListForm(pageFlowerLike.getContent());

        // 좋아요 누른 꽃이 없을 경우
        if(flowerList.isEmpty()) {
            throw new FlowerLikeException(ConstFlowerLike.FLOWER_LIKE_NOT_FOUND);
        }

        // 일정 범위의 페이지네이션을 보여주기 위한 변수
        int currentPage = pageFlowerLike.getNumber();
        int totalPages = pageFlowerLike.getTotalPages();
        // 시작 페이지 끝 페이지 계산
        PageDto pageDto = simpleCommonMethod.getPageDto(totalPages, currentPage);

        // DTO 변환
        BasicFlowerForm basicFlowerForm = BasicFlowerForm.builder()
                .flowerList(flowerList)
                .currentPage(currentPage)
                .totalPages(totalPages)
                .startPage(pageDto.getStartPage())
                .endPage(pageDto.getEndPage())
                .build();

        return basicFlowerForm;
    }
}
