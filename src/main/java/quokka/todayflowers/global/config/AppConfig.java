package quokka.todayflowers.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.client.RestTemplate;
import quokka.todayflowers.global.common.SimpleCommonMethod;
import quokka.todayflowers.global.common.SimpleConvert;

import java.time.Duration;

@Configuration
public class AppConfig {

    @Bean
    public SimpleConvert simpleConvert() {
        return new SimpleConvert();
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(2000)) // 연결 타임아웃
                .setReadTimeout(Duration.ofMillis(2000)) // 데이터 수신 타임아웃
                .build();

        return restTemplate;
    }

    @Bean
    public SimpleCommonMethod simpleCommonMethod() {
        return new SimpleCommonMethod();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CookieCsrfTokenRepository cookieCsrfTokenRepository() {
        // httpOnly true를 사용한다.
        return new CookieCsrfTokenRepository();
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }
}
