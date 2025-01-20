package com.fortune.app.card.dto;

import com.fortune.app.card.entity.Fortune;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FortuneRequestDto {
    @NotNull(message = "type is required.")
    private String type;

    public static FortuneRequestDto mapToDto(Fortune entity) {
        return FortuneRequestDto.builder()
                .type(entity.getType())
                .build();
    }
}
