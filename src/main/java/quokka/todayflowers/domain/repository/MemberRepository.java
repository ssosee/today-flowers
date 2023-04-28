package quokka.todayflowers.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quokka.todayflowers.domain.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserId(String userId);
    Optional<Member> findByUserIdAndPassword(String userId, String password);
    Optional<Member> findByUserIdAndPasswordAndEmail(String userId, String password, String email);
    Optional<Member> findByEmail(String email);
}
