package com.fortune.app.card.entity;

import com.fortune.app.card.dto.CardInterpretationDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "card_interpretation")
//@Getter
//@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardInterpretation {
    @Id
    @Column(name = "card_interpretation_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardInterpretationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fortune_id", nullable = false)
    private Fortune fortune;

    @Lob
    @Column(name = "interpretation_content", nullable = false)
    private String interpretationContent;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;

    @PrePersist
    public void prePersist() {
        this.createdAt = (this.createdAt == null) ? LocalDateTime.now() : this.createdAt;
        this.createdBy = (this.createdBy == null) ? 1 : this.createdBy;
    }

    public static CardInterpretation mapToEntity(CardInterpretationDto dto, Card card, Fortune fortune) {
        return CardInterpretation.builder()
                .card(card)
                .fortune(fortune)
                .interpretationContent(dto.getInterpretationContent())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getUpdatedBy())
                .build();
    }

    public Long getCardInterpretationId() {
        return cardInterpretationId;
    }

    public Card getCard() {
        return card;
    }

    public Fortune getFortune() {
        return fortune;
    }

    public String getInterpretationContent() {
        return interpretationContent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setFortune(Fortune fortune) {
        this.fortune = fortune;
    }

    public void setInterpretationContent(String interpretationContent) {
        this.interpretationContent = interpretationContent;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
}