package quokka.todayflowers.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import quokka.todayflowers.domain.entity.Flower;

import java.util.Optional;

public interface FlowerRepository extends JpaRepository<Flower, Long> {
    @Query("select f from Flower f" +
            " join fetch f.flowerPhotos fp" +
            " where f.month = :month" +
            " and f.day = :day")
    Optional<Flower> findFlowerAndFlowerPhotosByMonthAndDay(@Param("month") Integer month, @Param("day") Integer day);
}
