package com.fortune.app.daily.dto;

import com.fortune.app.daily.entity.CardLog;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardLogRequestDto {
    @NotNull(message = "cardInterpretationId is required.")
    private Long cardInterpretationId;

    @NotNull(message = "userId is required.")
    private Long userId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;

    public static CardLogRequestDto mapToDto(CardLog entity) {
        return CardLogRequestDto.builder()
                .cardInterpretationId(entity.getCardInterpretation().getCardInterpretationId())
                .userId(entity.getUser().getUserId())
                .build();
    }
}
