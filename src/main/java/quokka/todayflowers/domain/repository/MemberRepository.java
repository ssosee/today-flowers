package quokka.todayflowers.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import quokka.todayflowers.domain.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserId(String userId);
    Optional<Member> findByUserIdAndPassword(String userId, String password);
    List<Member> findByEmail(String email);
    Optional<Member> findByUserIdAndEmail(String userId, String email);
    @Query("select m from Member m" +
            " join fetch m.flowerLikes fl" +
            " where m.userId = :userId")
    Optional<Member> findMemberAndFlowerLikeByUserId(@Param("userId") String userId);
}
