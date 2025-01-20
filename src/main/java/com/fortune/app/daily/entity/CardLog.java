package com.fortune.app.daily.entity;

import com.fortune.app.card.entity.CardInterpretation;
import com.fortune.app.daily.dto.CardLogDto;
import com.fortune.app.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "card_log")
//@Getter
//@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardLog {
    @Id
    @Column(name = "card_log_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_interpretation_id", nullable = false)
    private CardInterpretation cardInterpretation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;

    public Long getCardLogId() {
        return cardLogId;
    }

    public CardInterpretation getCardInterpretation() {
        return cardInterpretation;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCardInterpretation(CardInterpretation cardInterpretation) {
        this.cardInterpretation = cardInterpretation;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = (this.createdAt == null) ? LocalDateTime.now() : this.createdAt;
        this.createdBy = (this.createdBy == null) ? 1 : this.createdBy;
    }

    public static CardLog mapToEntity(CardLogDto cardLogDto, CardInterpretation cardInterpretationDto, User userDto) {
        return CardLog.builder()
                .cardInterpretation(cardInterpretationDto)
                .user(userDto)
                .createdAt(cardLogDto.getCreatedAt())
                .updatedAt(cardLogDto.getUpdatedAt())
                .createdBy(cardLogDto.getCreatedBy())
                .updatedBy(cardLogDto.getUpdatedBy())
                .build();
    }
}