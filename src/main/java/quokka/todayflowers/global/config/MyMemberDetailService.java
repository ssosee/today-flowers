package quokka.todayflowers.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import quokka.todayflowers.domain.entity.Member;
import quokka.todayflowers.domain.repository.MemberRepository;
import quokka.todayflowers.global.constant.ConstMember;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MyMemberDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if(!StringUtils.hasText(username)) {
            throw new UsernameNotFoundException(ConstMember.MEMBER_NOT_FOUND);
        }

        Optional<Member> optionalMember = memberRepository.findByUserId(username);
        Member findMember = optionalMember.orElseThrow(() -> new BadCredentialsException(ConstMember.LOGIN_FAIL));

        return User.builder()
                .username(findMember.getUserId())
                .password(findMember.getPassword())
                .roles(findMember.getRole())
                .build();
    }
}
