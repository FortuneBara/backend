package com.fortune.app.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fortune.app.user.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.fortune.app.user.validation.UserValidationGroups.SignUpGroup;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {
    @NotNull(groups = SignUpGroup.class, message = "userId is required.")
    private Long userId;

    @NotNull(groups = SignUpGroup.class, message = "nickname is required.")
    private String nickname;

    @NotNull(groups = SignUpGroup.class, message = "email is required.")
    private String email;

    @NotNull(groups = SignUpGroup.class, message = "birth is required.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birth;

    public static UserRequestDto mapToDto(User entity) {
        return UserRequestDto.builder()
                .userId(entity.getUserId())
                .nickname(entity.getNickname())
                .email(entity.getEmail())
                .birth(entity.getBirth())
                .build();
    }
}