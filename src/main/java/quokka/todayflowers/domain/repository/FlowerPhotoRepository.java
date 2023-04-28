package quokka.todayflowers.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quokka.todayflowers.domain.entity.FlowerPhoto;

public interface FlowerPhotoRepository extends JpaRepository<FlowerPhoto, Long> {
}
