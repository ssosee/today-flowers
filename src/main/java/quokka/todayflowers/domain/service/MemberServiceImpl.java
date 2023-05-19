package quokka.todayflowers.domain.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import quokka.todayflowers.domain.entity.*;
import quokka.todayflowers.domain.repository.EmailLogRepository;
import quokka.todayflowers.domain.repository.FlowerLikeRepository;
import quokka.todayflowers.domain.repository.MemberRepository;
import quokka.todayflowers.global.constant.ConstEmail;
import quokka.todayflowers.global.constant.ConstMember;
import quokka.todayflowers.global.common.SimpleConvert;
import quokka.todayflowers.global.exception.ChangeEmailException;
import quokka.todayflowers.global.exception.ChangePasswordException;
import quokka.todayflowers.global.exception.JoinException;
import quokka.todayflowers.global.exception.CommonException;
import quokka.todayflowers.web.response.MyPageForm;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final FlowerLikeRepository flowerLikeRepository;
    private final EmailLogRepository emailLogRepository;
    private final PasswordEncoder passwordEncoder;
    private final SimpleConvert simpleConvert;


    // 메일 관련 빈
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final Context context;

    // 회원 가입
    @Override
    public void join(String userId, String password1, String password2, String email, SocialType socialType) {

        // 비밀번호가 다르면
        if(!password1.equals(password2)) {
            throw new JoinException(ConstMember.PASSWORD_NOT_SAME);
        }

        // 회원 조회(중복 회원 검증)
        Optional<Member> optionalMember = memberRepository.findByUserIdAndSocialType(userId, socialType);
        if(optionalMember.isPresent()) {
            throw new JoinException(ConstMember.DUPLICATE_USER_ID);
        }

        // 비밀번호 암호화
        String encode = passwordEncoder.encode(password1);
        // 회원 가입 수행
        Member member = Member.createNewMember(userId, encode, email);
        memberRepository.save(member);
    }

    // 로그인
    // 스프링 시큐리티 FormLogin, OAuth2Login으로 불필요함
    @Deprecated
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
    public void withdrawalMember(String userId) {
        // 회원 조회
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        Member findMember = optionalMember.orElseThrow(() -> new CommonException(ConstMember.MEMBER_NOT_FOUND));

        // 사용자가 누른 좋아요 꽃 목록
        List<FlowerLike> flowerLikes = flowerLikeRepository.findAllByMember(findMember);
        // 좋아요 감소
        for (FlowerLike flowerLike : flowerLikes) {
            flowerLike.getFlower().totalLikeLogic(false);
        }

        /**
         * 먼저 Member와 flowerLike는 양방향 관계이다.
         * 부모 엔티티(Member)를 먼저 삭제하면
         * flowerLike 에 UPDATE Query가 발생한다.
         * 그 이유는 자식 엔티티가 flowerLike인데
         * 부모 엔티티인 Member가 삭제되었으니,
         * 고아 객체가 된 flowerLike를 Hibernate가 반영하는 것 입니다.
         *
         * 이를 해결하기 위해서는 2가지 방법이 존재합니다.
         * 1. 자식 엔티티(FlowerLike)를 먼저 삭제한다.
         * 2. 자식과 연관관계를 끊은 후에, 부모 엔티티(Member)와 자식 엔티티(FlowerLike)를 삭제한다.
         *  ㄴ CASCADE, orphanRemoval 사용해하면 편리한데,
         *     현재 FlowerLike는 Member, Flower 두 테이블과 연관관계가 있어 부적절하다고 판단.
         */

        // 좋아요 삭제
        flowerLikeRepository.deleteAll(flowerLikes);
        // 회원 삭제
        memberRepository.delete(findMember);
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

        return MyPageForm.builder()
                .memberId(findMember.getId())
                .userId(findMember.getSocialType().equals(SocialType.NONE) ? findMember.getUserId() : findMember.getSocialName())
                .email(findMember.getEmail())
                .hits(findMember.getHits())
                .joinDate(simpleConvert.convertLocalDateTimeToString(findMember.getCreateDate()))
                .likeCount(findMember.getFlowerLikes().stream().count())
                .socialId(findMember.getUserId())
                .build();
    }

    // 방문 이력 증가
    @Override
    public void hitsUp(String userId) {
        Optional<Member> memberOptional = memberRepository.findByUserId(userId);
        Member findMember = memberOptional.orElseThrow(() -> new CommonException(ConstMember.MEMBER_NOT_FOUND));

        findMember.changeHits(1L);
    }

    // 메일 전송(임시 비밀번호 생성)
    @Async("emailAsyncExecutor")
    @Override
    public void sendMailForCreateTemporaryPassword(String userId, String fromEmail, String toEmail) {

        // 회원 검증
        Member findMember = validationMemberByUserIdAndEmail(userId, toEmail);

        try {

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
            String html = templateEngine.process("mail/authentication-mail", context);
            mimeMessageHelper.setText(html, true);

            // 메일 전송
            javaMailSender.send(mimeMessage);

            // 회원 비밀번호 임시 비밀번호로 변경
            findMember.changePassword(passwordEncoder.encode(authenticationNumber));

            // 만약 계정이 잠겨 있는 경우
            if (findMember.getLoginFailCount() >= 5) {
                // 계정 잠금 해제
                findMember.initLoginFailCount();
            }

            EmailLog emailLog = EmailLog.createEmailLog(EmailType.CHANGE_PASSWORD, ConstEmail.SUCCESS, findMember);
            emailLogRepository.save(emailLog);

        } catch (MessagingException e) {
            EmailLog emailLog = EmailLog.createEmailLog(EmailType.CHANGE_PASSWORD, ConstEmail.FAIL, findMember);
            emailLogRepository.save(emailLog);
        }
    }

    // 비밀번호 변경
    @Override
    public void changePassword(String userId, String email, String oldPassword, String newPassword) {

        // 회원 조회
        Optional<Member> optionalMember = memberRepository.findByUserIdAndEmail(userId, email);
        Member findMember = optionalMember.orElseThrow(() -> new ChangePasswordException(ConstMember.CHANGE_PASSWORD_FAIL_FOR_USERID_AND_EMAIL));

        // 비밀번호 확인
        boolean matches = passwordEncoder.matches(oldPassword, findMember.getPassword());
        if(!matches) {
            throw new ChangePasswordException(ConstMember.CHANGE_PASSWORD_FAIL_FOR_OLD_PASSWORD);
        }

        // 비밀번호 변경
        findMember.changePassword(passwordEncoder.encode(newPassword));
    }

    // 회원 검증
    private Member validationMemberByUserId(String userId) {
        // 회원 조회
        Optional<Member> optionalMember = memberRepository.findMemberAndFlowerLikeByUserId(userId);
        Member findMember = optionalMember.orElseThrow(() -> new CommonException(ConstMember.MEMBER_NOT_FOUND));

        return findMember;
    }

    // 회원 검증
    public Member validationMemberByUserIdAndEmail(String userId, String toEmail) {
        // 회원 조회
        Optional<Member> optionalMember = memberRepository.findByUserIdAndEmail(userId, toEmail);
        Member findMember = optionalMember.orElseThrow(() -> new CommonException(ConstMember.MEMBER_NOT_FOUND));

        return findMember;
    }

    // 이메일 변경
    @Override
    public void changeEmail(String userId, String email) {
        // 회원 조회
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        Member findMember = optionalMember.orElseThrow(() -> new ChangeEmailException(ConstMember.CHANGE_EMAIL_FAIL));

        // 이메일 변경
        findMember.changeEmail(email);
    }

    // 임시 비밀번호 생성
    private static String createAuthenticationNumber() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().substring(0, 8);
    }
}
