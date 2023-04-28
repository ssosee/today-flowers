package quokka.todayflowers.global.config;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.validation.annotation.Validated;
import quokka.todayflowers.global.constant.ConstMember;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * <a href="https://nahwasa.com/entry/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-30%EC%9D%B4%EC%83%81-Spring-Security-%EA%B8%B0%EB%B3%B8-%EC%84%B8%ED%8C%85-%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0">참고</a>
 */
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf().csrfTokenRepository(cookieCsrfTokenRepository()).and()
                .authorizeHttpRequests(request -> request
                    .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                    .requestMatchers("/user/login/**", "/user/home", "/user/signup", "/user/login-fail", "/css/**").permitAll()
                    .anyRequest().authenticated()
                );

        http
                .formLogin(login -> login
                        .loginPage("/user/login")
                        .loginProcessingUrl("/login-process")
                        .usernameParameter("user_id")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/home", true)
                        .failureHandler(new AuthenticationFailureHandler() {
                            @Override
                            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                                String errMessage = "";

                                if("POST".equalsIgnoreCase(request.getMethod())) {
                                    BufferedReader reader = request.getReader();
                                    String str = reader.readLine();

                                    if(str == null) errMessage = ConstMember.LOGIN_BLANK;
                                }
                                else if(exception instanceof BadCredentialsException) {
                                    errMessage = ConstMember.LOGIN_FAIL;
                                } else {
                                    errMessage = "관리자에게 문의 주세요.";
                                }

                                errMessage = URLEncoder.encode(errMessage, StandardCharsets.UTF_8);

                                response.sendRedirect("/user/login?error=true&exception="+errMessage);
                            }
                        })
                        .permitAll()
                )
                .logout(Customizer.withDefaults());

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
}
