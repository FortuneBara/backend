package com.fortune.app.oauth.controller;

import com.fortune.app.user.entity.User;
import com.fortune.app.user.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class OAuthController {
    private final UserRepository userRepository;

    public OAuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/signup")
    public String signupPage(@AuthenticationPrincipal OAuth2User oAuth2User, Model model) {
        String email = oAuth2User.getAttribute("email");

        Optional<User> userOptional = userRepository.findByEmailQueryDSL(email);
        if (userOptional.isPresent() && userOptional.get().getIsRegistered()) {
            return "redirect:/dashboard";
        }

        model.addAttribute("userId", userOptional.get().getUserId());
        model.addAttribute("email", email);
        return "signup";
    }
}
