package com.fortune.app.card.dto;

import com.fortune.app.card.entity.Card;
import com.fortune.app.enums.Orientation;
import lombok.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardDto {
    @NotNull(message = "cardId is required.")
    private Long cardId;
    @NotNull(message = "cardName is required.")
    private String name;
    @NotNull(message = "orientation is required.")
    private Orientation orientation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;

    public static CardDto mapToDto(Card entity) {
        return CardDto.builder()
                .cardId(entity.getCardId())
                .name(entity.getName())
                .orientation(entity.getOrientation())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }
}
