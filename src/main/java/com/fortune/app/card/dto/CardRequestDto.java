package com.fortune.app.card.dto;

import com.fortune.app.card.entity.Card;
import com.fortune.app.enums.Orientation;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardRequestDto {
    @NotNull(message = "name is required.")
    private String name;

    @NotNull(message = "orientation is required.")
    private Orientation orientation;

    public static CardRequestDto mapToDto(Card entity) {
        return CardRequestDto.builder()
                .name(entity.getName())
                .orientation(entity.getOrientation())
                .build();
    }
}
