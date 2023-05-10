package quokka.todayflowers.external.api.kakao.service;

import quokka.todayflowers.external.api.kakao.response.KakaoLoginAuthorizeResponse;
import quokka.todayflowers.external.api.kakao.response.KakaoTokenResponse;

import java.net.MalformedURLException;

public interface KakaoLoginService {
    // 카카오 인증 URL 생성
    String createKakaoAuthorizationURL();
    // 카카오 로그인 토크 받기
    KakaoTokenResponse getToken(String code) throws MalformedURLException;
    // 카카오 로그인 처리
    void kakaoLogin(KakaoTokenResponse response);
}
