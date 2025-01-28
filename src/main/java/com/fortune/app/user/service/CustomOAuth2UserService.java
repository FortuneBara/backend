package com.fortune.app.user.service;

import com.fortune.app.user.entity.User;
import com.fortune.app.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerUid = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String accessToken = userRequest.getAccessToken().getTokenValue();
        String name = oAuth2User.getAttribute("name");

        Optional<User> existingUser = userRepository.findByEmailQueryDSL(email);

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
            user.setAccessToken(accessToken);
        } else {
            user = User.builder()
                    .email(email)
                    .name(name)
                    .provider(provider)
                    .providerUid(providerUid)
                    .accessToken(accessToken)
                    .isRegistered(false)
                    .build();
        }

        userRepository.save(user);
        return oAuth2User;
    }
}
