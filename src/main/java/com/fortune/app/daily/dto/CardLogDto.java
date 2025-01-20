package com.fortune.app.daily.dto;

import com.fortune.app.daily.entity.CardLog;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardLogDto {
    @NotNull(message = "cardLogId is required.")
    private Long cardLogId;

    @NotNull(message = "cardInterpretationId is required.")
    private Long cardInterpretationId;

    @NotNull(message = "userId is required.")
    private Long userId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;

    public static CardLogDto mapToDto(CardLog entity) {
        return CardLogDto.builder()
                .cardLogId(entity.getCardLogId())
                .cardInterpretationId(entity.getCardInterpretation().getCardInterpretationId())
                .userId(entity.getUser().getUserId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }
}
