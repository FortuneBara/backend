package com.fortune.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/auth/signup", "/css/**", "/js/**", "/health").permitAll()  // /health 허용
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable);

        if (clientRegistrationRepository != null) {
            http.oauth2Login(oauth2 -> oauth2
                    .defaultSuccessUrl("/auth/signup", true)
            );
        }

        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/") // 로그아웃 후 메인 페이지로 이동
                .invalidateHttpSession(true) // 세션 무효화
                .clearAuthentication(true) // 인증 정보 삭제
                .deleteCookies("JSESSIONID", "SESSION") // 브라우저 쿠키 삭제
                .addLogoutHandler((request, response, authentication) -> {
                    request.getSession().invalidate(); // 세션 삭제
                })
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.sendRedirect("/login"); // 로그아웃 성공 시 리다이렉트
                })
        );

        return http.build();
    }
}
