package quokka.todayflowers.oauth2.user;

import lombok.Getter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class KakaoUser extends OAuth2ProviderUser {
    private final static String kakaoAccount = "kakao_account";
    private final static String properties = "properties"; //
    private final static String nickname = "nickname";
    private final static String email = "email";


    private Map<String, Object> kakaoAccountAttributes;
    private Map<String, Object> propertiesAttributes;

    public KakaoUser(OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super(oAuth2User, clientRegistration);
        this.kakaoAccountAttributes = (Map<String, Object>) oAuth2User.getAttributes().get(kakaoAccount);
        this.propertiesAttributes = (Map<String, Object>) oAuth2User.getAttributes().get(properties);
    }

    @Override
    public String getId() {
        return getAttributes().get("id").toString();
    }

    @Override
    public String getUserName() {
        return propertiesAttributes.get(nickname).toString();
    }

    @Override
    public String getEmail() {
        // 사용자가 선택적 동의
        if (kakaoAccountAttributes.get(email) == null) {
            return "";
        }
        return kakaoAccountAttributes.get(email).toString();
    }
}
