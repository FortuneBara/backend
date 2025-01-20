package com.fortune.app.card.entity;

import com.fortune.app.card.dto.FortuneDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "fortune")
//@Getter
//@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Fortune {
    @Id
    @Column(name = "fortune_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fortuneId;

    @Column(name = "type", nullable = false)
    private String type;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;

    @PrePersist
    public void prePersist() {
        this.createdAt = (this.createdAt == null) ? LocalDateTime.now() : this.createdAt;
        this.createdBy = (this.createdBy == null) ? 1 : this.createdBy;
    }

    public static Fortune mapToEntity(FortuneDto dto) {
        return Fortune.builder()
                .fortuneId(dto.getFortuneId())
                .type(dto.getType())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getUpdatedBy())
                .build();
    }

    public Long getFortuneId() {
        return fortuneId;
    }

    public String getType() {
        return type;
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


    public void setType(String type) {
        this.type = type;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

}
