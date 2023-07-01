package quokka.todayflowers.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quokka.todayflowers.domain.entity.Flower;
import quokka.todayflowers.domain.entity.FlowerLike;
import quokka.todayflowers.domain.entity.Member;
import quokka.todayflowers.domain.entity.SocialType;
import quokka.todayflowers.domain.repository.FlowerLikeRepository;
import quokka.todayflowers.domain.repository.FlowerRepository;
import quokka.todayflowers.domain.repository.MemberRepository;
import quokka.todayflowers.domain.service.response.TodayFlowerResponse;
import quokka.todayflowers.global.common.SimpleCommonMethod;
import quokka.todayflowers.global.constant.ConstFlower;
import quokka.todayflowers.global.constant.ConstMember;
import quokka.todayflowers.global.exception.*;
import quokka.todayflowers.web.response.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FlowerServiceImpl implements FlowerService {
    private final MemberRepository memberRepository;
    private final FlowerRepository flowerRepository;
    private final SimpleCommonMethod simpleCommonMethod;
    private final FlowerLikeRepository flowerLikeRepository;

    // 꽃 리스트 DTO로 변환
    private static List<FlowerListForm> getFlowerList(List<Flower> flowerList) {

        List<FlowerListForm> flowerListFormList = flowerList.stream()
                .map(fl -> FlowerListForm.builder()
                        .id(fl.getId())
                        .lang(fl.getFlowerLang())
                        .path(fl.getFlowerPhotos().get(0).getPath()) // 사진 1개만 표시
                        .totalLike(fl.getTotalLike())
                        .name(fl.getName())
                        .hits(fl.getHits())
                        .build()
                )
                .collect(Collectors.toList());

        return flowerListFormList;
    }

    // 회원이 꽃에 좋아요 눌렀는지 확인
    private boolean isFlowerLike(Flower findFlower, String userId) {
        Optional<FlowerLike> optionalFlowerLike = flowerLikeRepository.findByUserIdAndFlowerId(userId, findFlower.getId());
        Boolean like = false;

        // 좋아요
        if(optionalFlowerLike.isPresent()) {
            like = true;
        }

        return like;
    }

    /**
     * 오늘의 꽃 조회
     * 쿼리 3개 발생
     */
    @Override
    public TodayFlowerResponse findTodayFlower(String userId) {
        // 현재 시간 조회
        LocalDateTime now = LocalDateTime.now();
        int day = now.getDayOfMonth();
        int month = now.getMonthValue();

        // 오늘의 꽃 조회
        Optional<Flower> optionalFlower = flowerRepository.findFlowerByMonthAndDay(month, day);
        Flower findFlower = optionalFlower.orElseThrow(() -> new TodayException(ConstFlower.FLOWER_NOT_FOUND));

        // 조회수 증가
        findFlower.increaseHits();

        // 회원이 꽃에 좋아요 눌렀는지 확인
        boolean flowerLike = isFlowerLike(findFlower, userId);

        return TodayFlowerResponse.of(findFlower, userId, flowerLike);
    }

    // 쿼리 3개 발생
    @Override
    public TodayFlowerResponse findFlowerByFlowerId(Long flowerId, String userId) {

        // 아이디로 꽃 조회
        Optional<Flower> optionalFlower = flowerRepository.findFlowerById(flowerId);
        Flower findFlower = optionalFlower.orElseThrow(() -> new TodayException(ConstFlower.FLOWER_NOT_FOUND));

        // 조회수 증가
        findFlower.increaseHits();

        // 회원이 꽃에 좋아요 눌렀는지 확인
        boolean flowerLike = isFlowerLike(findFlower, userId);

        return TodayFlowerResponse.of(findFlower, userId, flowerLike);
    }

    // 생일의 꽃 조회
    @Override
    public BirthFlowerForm findBirthFlower(String birth, String userId) {
        // 입력값 자르기
        Integer month = Integer.parseInt(birth.substring(2, 4));
        Integer day = Integer.parseInt(birth.substring(4, 6));

        Optional<Flower> optionalFlower = flowerRepository.findFlowerByMonthAndDay(month, day);
        Flower findFlower = optionalFlower.orElseThrow(() -> new BirthException(ConstFlower.BIRTH_FLOWER_NOT_FOUND));

        // 조회수 증가
        findFlower.increaseHits();

        // 회원이 꽃에 좋아요 눌렀는지 확인
        Optional<FlowerLike> optionalFlowerLike = flowerLikeRepository.findByUserIdAndFlowerId(userId, findFlower.getId());

        Boolean like = false;
        // 좋아요
        if(optionalFlowerLike.isPresent()) {
            like = true;
        }

        // 회원 조회(시큐리티 컨텍스트에 userId가 있기 때문에 Member는 항상 존재)
        Optional<Member> findMember = memberRepository.findByUserId(userId);

        // dto로 변환
        BirthFlowerForm birthFlowerForm = BirthFlowerForm.builder()
                .flowerId(findFlower.getId())
                .flowerLang(findFlower.getFlowerLang())
                .photoPath(findFlower.getFlowerPhotos().stream()
                        .map(fp -> fp.getPath()).collect(Collectors.toList()))
                .totalLike(findFlower.getTotalLike())
                .name(findFlower.getName())
                .hits(findFlower.getHits())
                .description(findFlower.getDescription())
                .like(like)
                .userId(findMember.get().getSocialType().equals(SocialType.NONE) ? userId : findMember.get().getSocialName())
                .build();

        return birthFlowerForm;
    }


    // 좋아요
    @Override
    public FlowerLikeResponse likeFlower(Long flowerId, String userId, Boolean like) {

        // 꽃 조회
        Optional<Flower> optionalFlower = flowerRepository.findById(flowerId);
        Flower findFlower = optionalFlower.orElseThrow(() -> new BasicException(ConstFlower.FLOWER_NOT_FOUND));

        // 반대로 변경
        // false이면 true로 반환해야함(좋아요를 누른 것)
        // true이면 false로 반환(좋아요 취소를 누른 것)
        like = !like;
        // 좋아요 증감
        findFlower.totalLikeLogic(like);

        // 회원 조회
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        Member findMember = optionalMember.orElseThrow(() -> new BasicException(ConstMember.MEMBER_NOT_FOUND));

        // 좋아요 누른 꽃 조회
        Optional<FlowerLike> optionalFlowerLike = flowerLikeRepository.findByUserIdAndFlowerId(userId, flowerId);
        // 좋아요 누른 꽃이 없다면
        if(optionalFlowerLike.isEmpty()) {
            // 사용자가 좋아요 누른 꽃 생성
            FlowerLike flowerLike = FlowerLike.createFlowerLike(findFlower, findMember);
            // 연관관계 반영
            findMember.changeFlowerLike(flowerLike);
            // 사용자가 좋아요 누른 꽃 저장
            flowerLikeRepository.save(flowerLike);
        } else {
            FlowerLike flowerLike = optionalFlowerLike.get();
            // 사용자가 좋아요 누른 꽃 삭제
            flowerLikeRepository.delete(flowerLike);
        }

        // DTO 변환
        FlowerLikeResponse response = FlowerLikeResponse.builder()
                .message(ConstFlower.FLOWER_FOUND)
                .totalLikeCount(findFlower.getTotalLike())
                .like(like)
                .build();

        return response;
    }

    // 꽃말의 꽃 조회
    @Override
    public BasicFlowerForm findLangFlower(Pageable pageable, String lang) {

        // 꽃말로 꽃 조회
        Page<Flower> pageFlower;
        if(lang == null) {
            // 전체 꽃 조회
            pageFlower = flowerRepository.findFlowerByOrderByFlowerLang(pageable);
        } else {
            pageFlower = flowerRepository.findFlowerByFlowerLangContainingOrderByFlowerLang(pageable, lang);
        }

        // 꽃말을 가진 꽃이 없으면
        if(pageFlower.getContent().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("'").append(lang).append("'").append(ConstFlower.LANG_FLOWER_NOT_FOUND);

            throw new LangException(sb.toString());
        }

        // DTO 변환
        List<FlowerListForm> flowerList = getFlowerList(pageFlower.getContent());

        // 일정 범위의 페이지네이션을 보여주기 위한 변수
        int currentPage = pageFlower.getNumber();
        int totalPages = pageFlower.getTotalPages();
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

    @Override
    public BasicFlowerForm findNameFlower(Pageable pageable, String name) {
        // 꽃말로 꽃 조회
        Page<Flower> pageFlower;
        if(name == null) {
            // 전체 꽃 조회
            pageFlower = flowerRepository.findFlowerByOrderByName(pageable);
        } else {
            pageFlower = flowerRepository.findFlowerByNameContainingOrderByName(pageable, name);
        }

        // 꽃말을 가진 꽃이 없으면
        if(pageFlower.getContent().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("'").append(name).append("'").append(ConstFlower.NAME_FLOWER_NOT_FOUND);

            throw new NameException(sb.toString());
        }

        // DTO 변환
        List<FlowerListForm> flowerList = getFlowerList(pageFlower.getContent());

        // 일정 범위의 페이지네이션을 보여주기 위한 변수
        int currentPage = pageFlower.getNumber();
        int totalPages = pageFlower.getTotalPages();
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

    @Override
    public BasicFlowerForm findLikeFlowerRank(Pageable pageable) {
        // 좋아요 갯수 내림차순으로 조회
        Page<Flower> pageFlower = flowerRepository.findFlowerByOrderByTotalLikeDesc(pageable);
        // DTO로 변환
        List<FlowerListForm> flowerList = getFlowerList(pageFlower.getContent());

        // 일정 범위의 페이지네이션을 보여주기 위한 변수
        int currentPage = pageFlower.getNumber();
        int totalPages = pageFlower.getTotalPages();
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

