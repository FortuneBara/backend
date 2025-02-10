package com.fortune.app.user.service;

import com.fortune.app.jwt.JwtTokenProvider;
import com.fortune.app.user.entity.User;
import com.fortune.app.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerUid = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Optional<User> existingUser = userRepository.findByEmailQueryDSL(email);

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
            System.out.println("기존 유저 로그인: " + user.getEmail());
        } else {
            user = User.builder()
                    .email(email)
                    .name(name)
                    .provider(provider)
                    .providerUid(providerUid)
                    .isRegistered(false)
                    .build();
            user = userRepository.save(user);
        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), // 권한 부여
                oAuth2User.getAttributes(),
                "email" // 식별자
        );
    }
}
