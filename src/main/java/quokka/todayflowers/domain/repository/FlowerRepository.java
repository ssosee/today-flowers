package quokka.todayflowers.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quokka.todayflowers.domain.entity.Flower;

public interface FlowerRepository extends JpaRepository<Flower, Long> {
}
