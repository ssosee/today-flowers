package quokka.todayflowers.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 메일 전송 전용 결과 DB
 * 메일 전송이 비동기로 이루어짐
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailLog extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EmailType emailType;
    private String result;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static EmailLog createEmailLog(EmailType emailType, String result, Member member) {
        EmailLog emailLog = new EmailLog();

        emailLog.emailType = emailType;
        emailLog.result = result;
        emailLog.member = member;

        return emailLog;
    }
}
