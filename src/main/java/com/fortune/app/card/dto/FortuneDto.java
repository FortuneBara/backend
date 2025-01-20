package com.fortune.app.card.dto;

import com.fortune.app.card.entity.Fortune;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FortuneDto {
    @NotNull(message = "fortuneId is required.")
    private Long fortuneId;

    @NotNull(message = "type is required.")
    private String type;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;

    public static FortuneDto mapToDto(Fortune entity) {
        return FortuneDto.builder()
                .fortuneId(entity.getFortuneId())
                .type(entity.getType())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }
}
