package com.fortune.app.card.repostiory;

import com.fortune.app.card.entity.CardInterpretation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardInterpretationRepository extends JpaRepository<CardInterpretation, Long> {
    Optional<CardInterpretation> findByCardInterpretationId(Long cardInterpretationId);
}
