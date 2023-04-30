package quokka.todayflowers.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quokka.todayflowers.domain.entity.FlowerLike;
import quokka.todayflowers.domain.entity.Member;
import quokka.todayflowers.domain.repository.FlowerLikeRepository;
import quokka.todayflowers.domain.repository.MemberRepository;
import quokka.todayflowers.global.constant.ConstMember;
import quokka.todayflowers.global.convert.SimpleConvert;
import quokka.todayflowers.global.exception.BasicException;
import quokka.todayflowers.web.response.MyPageForm;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final FlowerLikeRepository flowerLikeRepository;
    private final PasswordEncoder passwordEncoder;
    private final SimpleConvert simpleConvert;

    // 회원 가입
    @Override
    public Boolean join(String userId, String password, String email) {

        Optional<Member> optionalMember = memberRepository.findByUserId(userId);

        // 해당 아이디로 이미 회원이 있다면
        if(optionalMember.isPresent()) {
            return false;
        }

        // 비밀번호 암호화
        String encode = passwordEncoder.encode(password);
        // 회원 가입 수행
        Member member = Member.createNewMember(userId, encode, email);
        memberRepository.save(member);

        return true;
    }

    // 로그인
    @Override
    public Boolean login(String userId, String password) {
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        Member findMember = optionalMember.orElseThrow(() -> new BasicException(ConstMember.MEMBER_NOT_FOUND));

        boolean matches = passwordEncoder.matches(password, findMember.getPassword());
        if(!matches) {
            return false;
        }

        return true;
    }

    // 로그아웃
    @Override
    public void logout(String userId) {

    }

    // 회원 삭제
    @Override
    public void withdrawalMember(String userId) {
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        Member findMember = optionalMember.orElseThrow(() -> new BasicException(ConstMember.MEMBER_NOT_FOUND));

        List<FlowerLike> flowerLikes = flowerLikeRepository.findAllByMember(findMember);

        // 삭제
        memberRepository.delete(findMember);
        flowerLikeRepository.deleteAll(flowerLikes);
    }

    // 회원 아이디 찾기
    @Override
    public List<String> findUserId(String email) {
        List<Member> findMembers = memberRepository.findByEmail(email);

        List<String> userIds = findMembers.stream()
                .map(m -> m.getUserId())
                .collect(Collectors.toList());

        return userIds;
    }

    // 내 정보
    @Override
    public MyPageForm findMember(String userId) {

        Optional<Member> memberOptional = memberRepository.findMemberAndFlowerLikeByUserId(userId);
        Member findMember = memberOptional.orElse(null);

        if(findMember == null) {
            return null;
        }

        MyPageForm myPageForm = MyPageForm.builder()
                .userId(findMember.getUserId())
                .email(findMember.getEmail())
                .hits(findMember.getHits())
                .joinDate(simpleConvert.convertLocalDateTimeToString(findMember.getCreateDate()))
                .likeCount(findMember.getFlowerLikes().stream().count())
                .build();

        return myPageForm;
    }

    @Override
    public void hitsUp(String userId) {
        Optional<Member> memberOptional = memberRepository.findByUserId(userId);
        Member findMember = memberOptional.orElseThrow(() -> new BasicException(ConstMember.MEMBER_NOT_FOUND));

        findMember.changeHits(1L);
    }
}
