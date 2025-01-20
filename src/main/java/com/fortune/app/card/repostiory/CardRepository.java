package com.fortune.app.card.repostiory;

import com.fortune.app.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByCardId(Long cardId);
}
