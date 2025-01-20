package com.fortune.app.card.dto;

import com.fortune.app.card.entity.CardInterpretation;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardInterpretationDto {
    @NotNull(message = "interpretationId is required.")
    private Long interpretationId;
    @NotNull(message = "cardId is required.")
    private Long cardId;
    @NotNull(message = "fortuneId is required.")
    private Long fortuneId;
    @NotNull(message = "interpretationContent is required.")
    private String interpretationContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;

    public static CardInterpretationDto mapToDto(CardInterpretation entity) {
        return CardInterpretationDto.builder()
                .interpretationId(entity.getCardInterpretationId())
                .cardId(entity.getCard().getCardId())
                .fortuneId(entity.getFortune().getFortuneId())
                .interpretationContent(entity.getInterpretationContent())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }
}