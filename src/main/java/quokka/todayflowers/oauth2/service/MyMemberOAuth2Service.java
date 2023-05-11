package quokka.todayflowers.oauth2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import quokka.todayflowers.domain.entity.SocialType;
import quokka.todayflowers.domain.repository.MemberRepository;
import quokka.todayflowers.domain.service.MemberService;
import quokka.todayflowers.oauth2.user.KakaoUser;
import quokka.todayflowers.oauth2.user.ProviderUser;

@Service
@RequiredArgsConstructor
public class MyMemberOAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberOAuth2Service memberOAuth2Service;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService= new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        ProviderUser provider = getProvider(clientRegistration, oAuth2User);
        Boolean join = memberOAuth2Service.join(provider.getId(),
                provider.getUserName(),
                provider.getPassword(),
                provider.getEmail(), SocialType.KAKAO);

        return oAuth2User;
    }

    private ProviderUser getProvider(ClientRegistration clientRegistration, OAuth2User oAuth2User) {
        if(clientRegistration.getRegistrationId().equalsIgnoreCase(SocialType.KAKAO.name())) {
            return new KakaoUser(oAuth2User, clientRegistration);
        }

        return null;
    }
}
