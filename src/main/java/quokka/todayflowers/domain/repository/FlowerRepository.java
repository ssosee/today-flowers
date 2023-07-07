package quokka.todayflowers.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import quokka.todayflowers.domain.entity.Flower;

import java.util.Optional;

/**
 * 컬렉션은 지연로딩으로 조회
 * hibernate.default_batch_fetch_size로 최적화
 * (컬렉션은 WHERE IN(? ? ... ? ?)으로 조회한다.)
 */
public interface FlowerRepository extends JpaRepository<Flower, Long> {
    @EntityGraph(attributePaths = {"flowerPhotos"})
    Optional<Flower> findFlowerByMonthAndDay(int month, int day);

    @Query("select f from Flower f" +
            " left join fetch FlowerPhoto fp" +
            " left join fetch FlowerLike fl" +
            " where f.month = :month" +
            " and f.day = :day" +
            " and fl.member.userId = :userId")
    Optional<Flower> findFlowerBy(@Param("month") int month,
                                  @Param("day") int day,
                                  @Param("userId") String userId);

    @EntityGraph(attributePaths = {"flowerPhotos"})
    Optional<Flower> findFlowerById(Long id);

    // 꽃 이름 오름차순 조회
    Page<Flower> findFlowerByOrderByName(Pageable pageable);

    // 꽃말 오름차순 조회
    Page<Flower> findFlowerByOrderByFlowerLang(Pageable pageable);

    // 꽃말로 오름차순 조회
    Page<Flower> findFlowerByFlowerLangContainingOrderByFlowerLang(Pageable pageable, String lang);
    // 꽃 이름으로 오름차순 조회
    Page<Flower> findFlowerByNameContainingOrderByName(Pageable pageable, String name);
    // 좋아요 많은 순서대로 조회
    Page<Flower> findFlowerByOrderByTotalLikeDesc(Pageable pageable);
}
