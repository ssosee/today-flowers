package quokka.todayflowers.domain.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
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

    /**
     * SELECT * FROM flower f
     * left join flower_photo fp on fp.flower_id  = f.id
     * left join flower_like fl on fl.flower_id = f.id
     * where fl.member_id = 2
     * and f.`month` = 7 and f.`day` = 1;
     * @param month
     * @param day
     * @param userId
     * @return
     */
    @Override
    public Optional<Flower> findFlowerBy(int month, int day, String userId) {
        Flower findFlower = queryFactory
                .selectFrom(flower)
                .leftJoin(flower.flowerPhotos, flowerPhoto).fetchJoin()
                .leftJoin(flowerLike).on(flowerLike.flower.eq(flower))
                .where(
                        flowerLike.member.userId.eq(userId),
                        flower.month.eq(month),
                        flower.day.eq(day)
                )
                .fetchOne();

        return Optional.ofNullable(findFlower);
    }
}
