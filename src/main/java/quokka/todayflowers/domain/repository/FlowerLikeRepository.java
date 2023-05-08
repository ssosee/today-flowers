package quokka.todayflowers.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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

    /**
     * 스프링 데이터 JPA에서 fetch join이 들어간 경우 Count 쿼리를 정상적으로 만들어내지 못한다.
     * 따라서 이경우 countQuery를 분리하여 사용해야한다.
     */
    @Query(value = "select fl from FlowerLike fl" +
            " left join fetch fl.flower f" +
            " where fl.member.userId = :userId",
    countQuery = "select count(fl) from FlowerLike fl" +
            " where fl.member.userId = :userId")
    Page<FlowerLike> findByUserId(Pageable pageable, @Param("userId") String userId);
}
