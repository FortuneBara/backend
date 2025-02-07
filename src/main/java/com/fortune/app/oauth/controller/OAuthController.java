package com.fortune.app.oauth.controller;

import com.fortune.app.enums.ErrorCode;
import com.fortune.app.jwt.JwtTokenProvider;
import com.fortune.app.user.dto.UserDto;
import com.fortune.app.user.entity.User;
import com.fortune.app.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class OAuthController {
    private final UserRepository userRepository;
    private final StringRedisTemplate redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    public OAuthController(UserRepository userRepository, StringRedisTemplate redisTemplate, JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(
            @CookieValue(value = "accessToken", required = false) String accessToken,
            HttpServletResponse response
    ) {
        if (accessToken == null || !jwtTokenProvider.validateToken(accessToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtTokenProvider.getEmailFromToken(accessToken);
        String refreshToken = redisTemplate.opsForValue().get(email);

        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(email);

        ResponseCookie cookie = ResponseCookie.from("accessToken", newAccessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/check-token")
    public ResponseEntity<?> checkToken(@CookieValue(value = "accessToken", required = false) String accessToken) {
        if (accessToken == null || !jwtTokenProvider.validateToken(accessToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorCode.INVALID_TOKEN);
        }

        String email = jwtTokenProvider.getEmailFromToken(accessToken);
        Optional<User> userOptional = userRepository.findByEmailQueryDSL(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorCode.INVALID_TOKEN);
        }

        User user = userOptional.get();
        UserDto userDto = UserDto.mapToDto(user);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo(@CookieValue(value = "accessToken", required = false) String accessToken) {
        if (accessToken == null || !jwtTokenProvider.validateToken(accessToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }

        String email = jwtTokenProvider.getEmailFromToken(accessToken);
        Optional<User> userOptional = userRepository.findByEmailQueryDSL(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("email", user.getEmail());
            userInfo.put("nickname", user.getNickname());
            return ResponseEntity.ok(userInfo);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0); // 즉시 만료
        response.addCookie(accessTokenCookie);

        return ResponseEntity.status(HttpStatus.OK).body("Logged out successfully");
    }
}
