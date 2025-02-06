package com.fortune.app.user.service;

import com.fortune.app.common.exception.CustomException;
import com.fortune.app.enums.ErrorCode;
import com.fortune.app.jwt.JwtTokenProvider;
import com.fortune.app.user.entity.User;
import com.fortune.app.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerUid = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Optional<User> existingUser = userRepository.findByEmailQueryDSL(email);

        User user;
        user = existingUser.orElseGet(() -> User.builder()
                .email(email)
                .name(name)
                .provider(provider)
                .providerUid(providerUid)
                .isRegistered(false)
                .build());

        String accessToken = jwtTokenProvider.createAccessToken(email);
        if (accessToken == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        String refreshToken = jwtTokenProvider.createRefreshToken(email);
        if (refreshToken == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        userRepository.save(user);

        return oAuth2User;
    }
}
