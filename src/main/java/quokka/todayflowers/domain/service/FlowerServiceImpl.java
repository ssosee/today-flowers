package quokka.todayflowers.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quokka.todayflowers.domain.entity.Flower;
import quokka.todayflowers.domain.entity.FlowerLike;
import quokka.todayflowers.domain.entity.Member;
import quokka.todayflowers.domain.repository.FlowerLikeRepository;
import quokka.todayflowers.domain.repository.FlowerRepository;
import quokka.todayflowers.domain.repository.MemberRepository;
import quokka.todayflowers.global.common.SimpleCommonMethod;
import quokka.todayflowers.global.constant.ConstFlower;
import quokka.todayflowers.global.constant.ConstMember;
import quokka.todayflowers.global.exception.BasicException;
import quokka.todayflowers.web.response.FlowerLikeResponse;
import quokka.todayflowers.web.response.TodayFlowerForm;

import java.time.LocalDateTime;
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

    @Override
    public TodayFlowerForm findTodayFlower() {
        // 현재 시간 조회
        LocalDateTime now = LocalDateTime.now();
        int day = now.getDayOfMonth();
        int month = now.getMonthValue();

        // 오늘의 꽃 조회
        Optional<Flower> optionalFlower = flowerRepository.findFlowerAndFlowerPhotosByMonthAndDay(month, day);
        Flower findFlower = optionalFlower.orElse(null);

        if (findFlower == null) return TodayFlowerForm.builder().build();

        // 조회수 증가
        findFlower.increaseHits();

        // 스프링시큐리티 컨테스트에서 userId 꺼내기
        String userId = simpleCommonMethod.getCurrentUserId();

        // 회원이 꽃에 좋아요 눌렀는지 확인
        Optional<FlowerLike> optionalFlowerLike = flowerLikeRepository.findByUserIdAndFlowerId(userId, findFlower.getId());
        FlowerLike findFlowerLike = optionalFlowerLike.orElse(null);

        Boolean like = false;
        // 좋아요
        if(findFlowerLike != null) {
            like = true;
        }

        // dto로 변환
        TodayFlowerForm todayFlowerForm = TodayFlowerForm.builder()
                .flowerId(findFlower.getId())
                .flowerLang(findFlower.getFlowerLang())
                .photoPath(findFlower.getFlowerPhotos().stream()
                        .map(fp -> fp.getPath()).collect(Collectors.toList()))
                .totalLike(findFlower.getTotalLike())
                .name(findFlower.getName())
                .hits(findFlower.getHits())
                .description(findFlower.getDescription())
                .userId(userId)
                .like(like)
                .build();

        return todayFlowerForm;
    }

    // 좋아요
    @Override
    public FlowerLikeResponse likeFlower(Long flowerId, Boolean like) {

        // 꽃 조회
        Optional<Flower> optionalFlower = flowerRepository.findById(flowerId);
        Flower findFlower = optionalFlower.orElseThrow(() -> new BasicException(ConstFlower.FLOWER_NOT_FOUND));

        // 반대로 변경
        // false이면 true로 반환해야함(좋아요를 누른 것)
        // true이면 false로 반환(좋아요 취소를 누른 것)
        like = !like;
        // 좋아요 증감
        findFlower.totalLikeLogic(like);

        // 스프링시큐리티 컨테스트에서 userId 꺼내기
        String userId = simpleCommonMethod.getCurrentUserId();
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

        FlowerLikeResponse response = FlowerLikeResponse.builder()
                .message(ConstFlower.FLOWER_FOUND)
                .totalLikeCount(findFlower.getTotalLike())
                .like(like)
                .build();

        return response;
    }
}

