package quokka.todayflowers.global.config;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.io.IOException;

/**
 * <a href="https://nahwasa.com/entry/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-30%EC%9D%B4%EC%83%81-Spring-Security-%EA%B8%B0%EB%B3%B8-%EC%84%B8%ED%8C%85-%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0">참고</a>
 */
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf().csrfTokenRepository(cookieCsrfTokenRepository()).and()
                .authorizeHttpRequests(request -> request
                    .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                    .requestMatchers("/home",
                            "/user/login/**", "/user/signup", "/user/login-fail", "/user/find-userId", "/user/find-password",
                            "/today-flower/today",
                            "/css/**", "/image/**").permitAll()
                    .anyRequest().authenticated()
                );

        // 로그인 설정
        http
                .formLogin(login -> login
                        .loginPage("/user/login")
                        .loginProcessingUrl("/login-process")
                        .usernameParameter("user_id")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/home", true)
                        .failureHandler(authenticationFailureHandler()) // 로그인 실패 처리
                        .permitAll()
                );

        // 자동 로그인 설정
        http.rememberMe()
                .rememberMeParameter("remember")
                .alwaysRemember(false)
                .userDetailsService(userDetailsService);

        // 로그아웃 설정
        http
                .logout()
                .logoutUrl("/logout-process")
                .logoutSuccessUrl("/user/login")
                .deleteCookies("remember-me");

        // 세션 정책 설정
        http
                .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false) // 현재 사용자 로그인, 기존 사용자 로그아웃
                .expiredUrl("/invalid");

        return http.build();
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

    // 로그인 실패 처리 핸들러
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new SimpleAuthenticationFailureHandler();
    }
}
