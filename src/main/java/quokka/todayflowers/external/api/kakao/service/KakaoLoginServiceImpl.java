package quokka.todayflowers.external.api.kakao.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import quokka.todayflowers.external.api.kakao.request.KakaoTokenRequest;
import quokka.todayflowers.external.api.kakao.response.KakaoLoginAuthorizeResponse;
import quokka.todayflowers.external.api.kakao.response.KakaoTokenResponse;
import quokka.todayflowers.external.api.kakao.response.KakaoUserInfoResponse;

import java.net.MalformedURLException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class KakaoLoginServiceImpl implements KakaoLoginService {

    private final RestTemplate restTemplate;
    // 인가 코드 host
    @Value("${kakao.authorization-host}")
    private String kakaoAuthorizationHost;
    // 인가 토큰 host
    @Value("${kakao.token-host}")
    private String kakaoTokenHost;
    // 사용자 정보 가져오기 host
    @Value("${kakao.user-host}")
    private String kakaoUserHost;
    @Value("${kakao.rest-api-key}")
    private String REST_API_KEY;
    @Value("${kakao.client-secret}")
    private String CLIENT_SECRET;
    @Value("${redirect-uri}")
    private String REDIRECT_URI;

    private String clientId = "client_id";
    private String redirectUri = "redirect_uri";
    private String responseType = "response_type";
    private String grantType = "grant_type";
    private String code = "code";
    private String clientSecret = "client_secret";

    // 인가 코드 받는 URL 생성
    @Override
    public String createKakaoAuthorizationURL() {
        try {
            URL url = new URL(kakaoAuthorizationHost);
            StringBuffer sb = new StringBuffer();
            sb.append(url);
            sb.append("?").append(clientId).append("=").append(REST_API_KEY);
            sb.append("&").append(redirectUri).append("=").append(REDIRECT_URI);
            sb.append("&").append(responseType).append("=").append("code");

            return sb.toString();

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    // 인가 요청에서 토큰 얻기
    @Override
    public KakaoTokenResponse getToken(String code) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        StringBuffer sb = new StringBuffer();
        sb.append(grantType).append("=").append("authorization_code").append("&"); // authorization_code 으로 고정
        sb.append(clientId).append("=").append(REST_API_KEY).append("&");
        sb.append(redirectUri).append("=").append(REDIRECT_URI).append("&"); // 인가 코드가 리다이렉트된 URI
        sb.append(this.code).append("=").append(code).append("&");
        sb.append(clientSecret).append("=").append(CLIENT_SECRET);

        HttpEntity request = new HttpEntity(sb.toString(), httpHeaders);

        // 카카오 서버로 인가 코드 요청
        ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(kakaoTokenHost, HttpMethod.POST, request, KakaoTokenResponse.class);

        return response.getBody();
    }


    // 회원가입 처리
    @Override
    public void kakaoLogin(KakaoTokenResponse response) {
        // 회원 정보 가져오기
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBearerAuth(response.getAccessToken());

        HttpEntity request = new HttpEntity(httpHeaders);

        ResponseEntity<String> exchange = restTemplate.exchange(kakaoUserHost, HttpMethod.GET, request, String.class);


    }
}
