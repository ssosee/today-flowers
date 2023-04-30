package quokka.todayflowers.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quokka.todayflowers.domain.entity.FlowerLike;
import quokka.todayflowers.domain.entity.Member;

import java.util.List;

public interface FlowerLikeRepository extends JpaRepository<FlowerLike, Long> {
    List<FlowerLike> findAllByMember(Member member);
}
