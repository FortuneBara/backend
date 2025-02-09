package com.fortune.app.oauth.handler;

import com.fortune.app.jwt.JwtTokenProvider;
import com.fortune.app.user.entity.User;
import com.fortune.app.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public OAuth2LoginSuccessHandler(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String frontendUrl = System.getenv("FRONTEND_URL") != null ? System.getenv("FRONTEND_URL") : "http://localhost:3000";

        Optional<User> userOptional = userRepository.findByEmailQueryDSL(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            String accessToken = jwtTokenProvider.createAccessToken(user.getEmail());

            Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setSecure(true);
            accessTokenCookie.setPath("/");
            accessTokenCookie.setMaxAge(60 * 60 * 24); // 1일
            accessTokenCookie.setAttribute("SameSite", "None");
            response.addCookie(accessTokenCookie);

            if (user.getIsRegistered()) {
                response.sendRedirect(frontendUrl + "/profile");
            } else {
                response.sendRedirect(frontendUrl + "/signup?userId=" + user.getUserId() + "&email=" + user.getEmail());
            }
        } else {
            response.sendRedirect(frontendUrl + "/login?error=user_not_found");
        }
    }
}
