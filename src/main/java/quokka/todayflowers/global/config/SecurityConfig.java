package quokka.todayflowers.global.config;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.filter.CorsFilter;
import quokka.todayflowers.global.config.handler.SimpleFormLoginAuthenticationFailureHandler;
import quokka.todayflowers.global.config.handler.SimpleFormLoginAuthenticationSuccessHandler;
import quokka.todayflowers.global.config.handler.SimpleOAuth2LoginAuthenticationSuccessHandler;
import quokka.todayflowers.oauth2.service.CustomMemberOAuth2Service;

import java.io.IOException;

/**
 * <a href="https://nahwasa.com/entry/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-30%EC%9D%B4%EC%83%81-Spring-Security-%EA%B8%B0%EB%B3%B8-%EC%84%B8%ED%8C%85-%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0">참고</a>
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    // 로그인 실패 처리 핸들러
    private final SimpleFormLoginAuthenticationFailureHandler simpleFormLoginAuthenticationFailureHandler;
    // 로그인 성공 처리 핸들러
    private final SimpleFormLoginAuthenticationSuccessHandler simpleFormLoginAuthenticationSuccessHandler;
    private final SimpleOAuth2LoginAuthenticationSuccessHandler simpleOAuth2LoginAuthenticationSuccessHandler;
    private final CorsFilter corsFilter;

    private final CustomMemberOAuth2Service customMemberOAuth2Service;
    private final CustomMemberDetailService customMemberDetailService;
    private final CookieCsrfTokenRepository cookieCsrfTokenRepository;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.headers().httpStrictTransportSecurity().disable();

//        http
//                .headers()
//                .httpStrictTransportSecurity()
//                .maxAgeInSeconds(31536000)
//                .includeSubDomains(true)
//                .preload(true);

        http
                .addFilter(corsFilter)
                .csrf().csrfTokenRepository(cookieCsrfTokenRepository).and()
                .authorizeHttpRequests(requests -> requests
                        .shouldFilterAllDispatcherTypes(false)
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .requestMatchers("/",
                                "/profile",
                                "/user/invalid",
                                "/user/login/**", "/user/signup", "/user/login-fail", "/user/find-userId", "/user/find-password", "/user/send-email",
                                "/today-flower/today",
                                "/kakao/user/**",
                                "/css/**", "/image/**", "/error").permitAll()
                        .anyRequest().authenticated()
                );

        // formLogin 로그인 설정
        http
                .formLogin(login -> login
                                .loginPage("/user/login")
                                .loginProcessingUrl("/login-process")
                                .usernameParameter("userId")
                                .passwordParameter("password")
                                .successHandler(simpleFormLoginAuthenticationSuccessHandler)
                                .failureHandler(simpleFormLoginAuthenticationFailureHandler) // 로그인 실패 처리
                                .permitAll()
                );

        // oauth2Login 설정
        http.oauth2Login(oauth2 -> oauth2.userInfoEndpoint(
                userInfoEndpointConfig -> userInfoEndpointConfig.userService(customMemberOAuth2Service))
                .successHandler(simpleOAuth2LoginAuthenticationSuccessHandler));

        http.userDetailsService(customMemberDetailService);


        // 자동 로그인 설정
        http.rememberMe()
                .rememberMeParameter("remember")
                .alwaysRemember(false);

        // 로그아웃 설정
        http
                .logout()
                .logoutUrl("/logout-process")
                .logoutSuccessUrl("/user/login")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("remember-me")
                .deleteCookies("JSESSIONID");

        // 세션 정책 설정
        http
                .sessionManagement()
                .sessionFixation().changeSessionId() // 세션 고정 보호 로그인할때 마다 JSESSIONID를 변경
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false) // 기존 사용자 로그아웃, 현재 사용자 로그인
                .expiredUrl("/user/invalid");

        http.exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/user/login"));

        return http.build();
    }
}
