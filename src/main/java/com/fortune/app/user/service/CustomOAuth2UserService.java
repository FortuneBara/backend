package com.fortune.app.user.service;

import com.fortune.app.jwt.JwtTokenProvider;
import com.fortune.app.user.entity.User;
import com.fortune.app.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        log.info("🔄 OAuth2 로그인 요청 수신됨");

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("✅ OAuth2 사용자 정보 가져옴: {}", oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerUid = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        log.info("📌 Provider: {}", provider);
        log.info("📌 Provider UID: {}", providerUid);
        log.info("📌 Email: {}", email);
        log.info("📌 Name: {}", name);

        Optional<User> existingUser = userRepository.findByEmailQueryDSL(email);

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
            log.info("🔄 기존 사용자 로그인: {}", user.getEmail());
        } else {
            log.info("🚀 새로운 사용자 저장 중...");
            user = User.builder()
                    .email(email)
                    .name(name)
                    .provider(provider)
                    .providerUid(providerUid)
                    .isRegistered(false)
                    .build();
            user = userRepository.save(user);
            userRepository.flush();
            log.info("✅ 사용자 저장 완료: {}", user.getEmail());
        }

        log.info("🔐 사용자 인증 객체 생성 완료");
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2User.getAttributes(),
                "email"
        );
    }
}