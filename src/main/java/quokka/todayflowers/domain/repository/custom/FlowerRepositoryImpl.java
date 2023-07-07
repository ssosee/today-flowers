package quokka.todayflowers.domain.repository.custom;

import com.ctc.wstx.util.StringUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import quokka.todayflowers.domain.entity.Flower;
import quokka.todayflowers.domain.entity.QFlower;

import java.util.List;
import java.util.Optional;

import static quokka.todayflowers.domain.entity.QFlower.*;
import static quokka.todayflowers.domain.entity.QFlowerLike.flowerLike;
import static quokka.todayflowers.domain.entity.QFlowerPhoto.flowerPhoto;


@Repository
@RequiredArgsConstructor
public class FlowerRepositoryImpl implements FlowerRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Flower> findFlowerBy(int month, int day, String userId) {
        Flower findFlower = queryFactory
                .selectFrom(flower)
                .leftJoin(flower.flowerPhotos, flowerPhoto).fetchJoin()
                .leftJoin(flowerLike).on(flowerLike.flower.eq(flower))
                .where(
                        eqUserId(userId),
                        flower.month.eq(month),
                        flower.day.eq(day)
                )
                .fetchOne();

        return Optional.ofNullable(findFlower);
    }

    private BooleanExpression eqUserId(String userId) {
        return (userId.equals("anonymousUser") || !StringUtils.hasText(userId)) ? null : flowerLike.member.userId.eq(userId);
    }
}
