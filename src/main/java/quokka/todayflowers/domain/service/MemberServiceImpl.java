package quokka.todayflowers.domain.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import quokka.todayflowers.domain.entity.FlowerLike;
import quokka.todayflowers.domain.entity.Member;
import quokka.todayflowers.domain.repository.FlowerLikeRepository;
import quokka.todayflowers.domain.repository.MemberRepository;
import quokka.todayflowers.global.constant.ConstMember;
import quokka.todayflowers.global.common.SimpleConvert;
import quokka.todayflowers.global.exception.BasicException;
import quokka.todayflowers.web.response.MyPageForm;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final FlowerLikeRepository flowerLikeRepository;
    private final PasswordEncoder passwordEncoder;
    private final SimpleConvert simpleConvert;

    // 메일 관련 빈
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final Context context;

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
        Member findMember = optionalMember.orElse(null);

        if(findMember == null) {
            return false;
        }

        boolean matches = passwordEncoder.matches(password, findMember.getPassword());
        if(!matches || findMember == null) {
            return false;
        }

        return true;
    }

    // 회원 삭제
    @Override
    public Boolean withdrawalMember(String userId) {
        // 회원 조회
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        Member findMember = optionalMember.orElse(null);

        // 회원이 없으면
        if(findMember == null) {
            return false;
        }

        // 사용자가 누른 좋아요 꽃 목록
        List<FlowerLike> flowerLikes = flowerLikeRepository.findAllByMember(findMember);

        // 회원 삭제
        memberRepository.delete(findMember);
        // 좋아요 삭제
        flowerLikeRepository.deleteAll(flowerLikes);

        return true;
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

        // 회원 검증
        Member findMember = validationMemberByUserId(userId);

        if(findMember == null) {
            return new MyPageForm();
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

    // 방문 이력 증가
    @Override
    public void hitsUp(String userId) {
        Optional<Member> memberOptional = memberRepository.findByUserId(userId);
        Member findMember = memberOptional.orElseThrow(() -> new BasicException(ConstMember.MEMBER_NOT_FOUND));

        findMember.changeHits(1L);
    }

    // 메일 전송(임시 비밀번호 생성)
    @Override
    public Boolean sendMailForCreateTemporaryPassword(String userId, String fromEmail, String toEmail)  {
        try {
            // 회원 검증
            Member findMember = validationMemberByUserIdAndEmail(userId, toEmail);
            if(findMember == null) {
                return false;
            }

            // 메일 발신 로직
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            // 제목 설정
            mimeMessageHelper.setSubject(ConstMember.AUTHENTICATION_EMAIL_SUBJECT);
            // 발신자
            mimeMessageHelper.setFrom(fromEmail);
            // 수신자
            mimeMessageHelper.setTo(toEmail);

            // 임시 비밀번호 생성
            String authenticationNumber = createAuthenticationNumber();
            // 템플릿에 전달할 데이터 설정
            context.setVariable("authenticationNumber", authenticationNumber);

            // html 생성
            String html = templateEngine.process("/mail/authentication-mail", context);
            mimeMessageHelper.setText(html, true);

            // 메일 전송
            javaMailSender.send(mimeMessage);

            // 회원 비밀번호 임시 비밀번호로 변경
            findMember.changePassword(passwordEncoder.encode(authenticationNumber));

            // 만약 계정이 잠겨 있는 경우
            if(findMember.getLoginFailCount() >= 5) {
                // 계정 잠금 해제
                findMember.initLoginFailCount();
            }

            return true;

        } catch (MessagingException e) {
            return false;
        }
    }

    // 비밀번호 변경
    @Override
    public Boolean changePassword(String userId, String email, String oldPassword, String newPassword) {

        // 회원 조회
        Member findMember = validationMemberByUserIdAndEmail(userId, email);
        if (findMember == null) {
            return false;
        }

        // 비밀번호 확인
        boolean matches = passwordEncoder.matches(oldPassword, findMember.getPassword());
        if(!matches) {
            return false;
        }

        // 비밀번호 변경
        findMember.changePassword(passwordEncoder.encode(newPassword));

        return true;
    }

    // 회원 검증
    private Member validationMemberByUserId(String userId) {
        // 회원 조회
        Optional<Member> memberOptional = memberRepository.findMemberAndFlowerLikeByUserId(userId);
        Member findMember = memberOptional.orElse(null);

        // 회원이 없으면
        if(findMember == null) {
            return null;
        }

        return findMember;
    }

    // 회원 검증
    private Member validationMemberByUserIdAndEmail(String userId, String toEmail) {
        // 회원 조회
        Optional<Member> optionalMember = memberRepository.findByUserIdAndEmail(userId, toEmail);
        Member findMember = optionalMember.orElse(null);

        // 회원이 없으면
        if(findMember == null) {
            return null;
        }

        return findMember;
    }

    // 임시 비밀번호 생성
    private String createAuthenticationNumber() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().substring(0, 8);
    }
}
