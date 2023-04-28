package quokka.todayflowers.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quokka.todayflowers.domain.entity.FlowerLike;

public interface FlowerLikeRepository extends JpaRepository<FlowerLike, Long> {
}
