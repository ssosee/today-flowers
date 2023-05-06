package quokka.todayflowers.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import quokka.todayflowers.domain.entity.Flower;

import java.util.Optional;

public interface FlowerRepository extends JpaRepository<Flower, Long> {
    @Query("select f from Flower f" +
            " join fetch f.flowerPhotos fp" +
            " where f.month = :month" +
            " and f.day = :day")
    Optional<Flower> findFlowerAndFlowerPhotosByMonthAndDay(@Param("month") Integer month, @Param("day") Integer day);

    // 컬렉션은 지연로딩으로 조회
    // hibernate.default_batch_fetch_size로 최적화
    Page<Flower> findFlowerByOrderByNameDesc(Pageable pageable);

    // 꽃말로 조회
    Page<Flower> findFlowerByFlowerLangContainingOrderByFlowerLang(Pageable pageable, String lang);
}
