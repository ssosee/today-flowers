package quokka.todayflowers.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quokka.todayflowers.domain.entity.EmailLog;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
}
