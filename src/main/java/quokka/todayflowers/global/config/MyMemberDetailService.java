package quokka.todayflowers.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import quokka.todayflowers.domain.entity.Member;
import quokka.todayflowers.domain.repository.MemberRepository;
import quokka.todayflowers.global.constant.ConstMember;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 스프링 시큐리티 사용시 DB 연동
 */
@Component
@RequiredArgsConstructor
@Transactional
public class MyMemberDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> optionalMember = memberRepository.findByUserId(username);
        // DB에 회원 정보가 없을 경우 예외 발생
        Member findMember = optionalMember.orElseThrow(() -> new BadCredentialsException(ConstMember.LOGIN_FAIL));

        loginProcess(findMember);

        return User.builder()
                .username(findMember.getUserId())
                .password(findMember.getPassword())
                .roles(findMember.getRole())
                .build();
    }

    private void loginProcess(Member findMember) throws UsernameNotFoundException {
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
