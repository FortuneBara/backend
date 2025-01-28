package com.fortune.app.dashboard.controller;

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
@RequestMapping("/dashboard")
public class DashboardController {

    private final UserRepository userRepository;

    public DashboardController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String dashboard(@AuthenticationPrincipal OAuth2User oAuth2User, Model model) {
        if (oAuth2User == null) {
            return "redirect:/login";
        }

        String email = oAuth2User.getAttribute("email");
        Optional<User> userOptional = userRepository.findByEmailQueryDSL(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("userId", user.getUserId());
            model.addAttribute("nickname", user.getNickname());
            model.addAttribute("email", user.getEmail());
            return "dashboard";
        } else {
            return "redirect:/auth/signup";
        }
    }
}
