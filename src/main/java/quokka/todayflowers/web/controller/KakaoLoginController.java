package quokka.todayflowers.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import quokka.todayflowers.external.api.kakao.response.KakaoTokenResponse;
import quokka.todayflowers.external.api.kakao.service.KakaoLoginService;

import java.net.MalformedURLException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/kakao/user")
public class KakaoLoginController {

    //private String kakaoUrl = "https://kauth.kakao.com/oauth/authorize?client_id=80cc8b82bb623aa1a2e5da13caab1ca9&redirect_uri=http://localhost:8080/kakao/user/signup&response_type=code";
    private final KakaoLoginService kakaoLoginService;

    @GetMapping("/login")
    public String kakaoLogin() {
        return "redirect:"+kakaoLoginService.createKakaoAuthorizationURL();
    }

    // 카카오 인가 코드 받기
    @GetMapping(value = "/signup")
    public String kakaoSignup(@RequestParam(value = "code", required = false) String code) throws MalformedURLException {

        // 토큰 발급
        KakaoTokenResponse response = kakaoLoginService.getToken(code);
        kakaoLoginService.kakaoLogin(response);

        return "/home";
    }

}
