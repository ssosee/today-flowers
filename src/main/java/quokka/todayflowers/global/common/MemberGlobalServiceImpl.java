package quokka.todayflowers.global.common;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quokka.todayflowers.domain.entity.Member;
import quokka.todayflowers.global.constant.ConstMember;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Transactional
public class MemberGlobalServiceImpl implements MemberGlobalService {
    public void loginProcess(Member findMember) throws UsernameNotFoundException {
        // 로그인 5회 이상 실패한 계정인 경우
        if(findMember.getLoginFailCount() >= 5) {
            throw new InternalAuthenticationServiceException(ConstMember.LOCK_MEMBER);
        }
        // 로그인 이력이 없으면
        else if(findMember.getLoginDate() == null) {
            findMember.setLogin();
        } else {
            // 방문횟수 증가
            Duration duration = Duration.between(findMember.getLoginDate(), LocalDateTime.now());
            long diffSeconds = duration.getSeconds();
            // 30분 이후 로그인할 시
            if(diffSeconds > 60 * 30) {
                findMember.setLogin();
            }
        }
    }
}
