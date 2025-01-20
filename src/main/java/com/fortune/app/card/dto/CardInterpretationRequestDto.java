package com.fortune.app.card.dto;

import com.fortune.app.card.entity.CardInterpretation;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardInterpretationRequestDto {
    @NotNull(message = "cardId is required.")
    private Long cardId;

    @NotNull(message = "fortuneId is required.")
    private Long fortuneId;

    @NotNull(message = "interpretationContent is required.")
    private String interpretationContent;

    public static CardInterpretationRequestDto mapToDto(CardInterpretation entity) {
        return CardInterpretationRequestDto.builder()
                .cardId(entity.getCard().getCardId())
                .fortuneId(entity.getFortune().getFortuneId())
                .interpretationContent(entity.getInterpretationContent())
                .build();
    }
}