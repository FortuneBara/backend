package com.fortune.app.card.repostiory;

import com.fortune.app.card.entity.Fortune;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FortuneRepository extends JpaRepository<Fortune, Long> {
    Optional<Fortune> findByFortuneId(Long fortuneId);
}
