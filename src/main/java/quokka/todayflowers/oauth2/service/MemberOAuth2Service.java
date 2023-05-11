package quokka.todayflowers.oauth2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quokka.todayflowers.domain.entity.Member;
import quokka.todayflowers.domain.entity.SocialType;
import quokka.todayflowers.domain.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberOAuth2Service {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member join(String userId, String socialName, String password1, String email, SocialType socialType) {

        Optional<Member> optionalMember = memberRepository.findByUserIdAndSocialType(userId, socialType);

        // 해당 아이디로 이미 회원이 있다면
        if(optionalMember.isPresent()) {
            return optionalMember.get();
        }

        // 비밀번호 암호화
        String encode = passwordEncoder.encode(password1);
        // 회원 가입 수행
        Member member = Member.createSocialMember(userId, socialName, encode, email, socialType);
        memberRepository.save(member);

        return member;
    }
}
