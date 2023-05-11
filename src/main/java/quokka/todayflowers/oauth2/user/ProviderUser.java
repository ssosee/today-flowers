package quokka.todayflowers.oauth2.user;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Map;

public interface ProviderUser {
    String getId();
    String getUserName();
    String getPassword();
    String getEmail();
    String getProvider();
    List<? extends GrantedAuthority> getAuthorities();
    // 서비스 제공자로 부터 받는 값들
    Map<String, Object> getAttributes();
}
