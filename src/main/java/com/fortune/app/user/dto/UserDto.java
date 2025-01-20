package com.fortune.app.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fortune.app.user.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.time.LocalDateTime;
import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    // User
    @NotNull(message = "userId is required.")
    private Long userId;

    @NotNull(message = "name is required.")
    private String name;

    private String email;

    private String nickname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birth;

    // Oauth
    @NotNull(message = "provider is required.")
    private String provider;

    @NotNull(message = "providerUid is required.")
    private String providerUid;

    @NotNull(message = "accessToken is required.")
    private String accessToken;

    private String refreshToken;

    private Boolean isRegistered;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;

    public static UserDto mapToDto(User entity) {
        return UserDto.builder()
                .userId(entity.getUserId())
                .name(entity.getName())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .birth(entity.getBirth())
                .provider(entity.getProvider())
                .providerUid(entity.getProviderUid())
                .accessToken(entity.getAccessToken())
                .refreshToken(entity.getRefreshToken())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }
}