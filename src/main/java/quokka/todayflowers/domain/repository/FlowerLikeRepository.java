package quokka.todayflowers.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import quokka.todayflowers.domain.entity.FlowerLike;
import quokka.todayflowers.domain.entity.Member;

import java.util.List;
import java.util.Optional;

public interface FlowerLikeRepository extends JpaRepository<FlowerLike, Long> {
    List<FlowerLike> findAllByMember(Member member);
    @Query("select fl from FlowerLike fl" +
            " where fl.member.userId = :userId" +
            " and fl.flower.id = :flowerId")
    Optional<FlowerLike> findByUserIdAndFlowerId(@Param("userId") String userId, @Param("flowerId") Long flowerId);
}
