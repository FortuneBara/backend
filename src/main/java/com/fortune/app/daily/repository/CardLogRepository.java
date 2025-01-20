package com.fortune.app.daily.repository;

import com.fortune.app.daily.entity.CardLog;
import com.fortune.app.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardLogRepository extends JpaRepository<CardLog, Long> {
    Optional<CardLog> findByCardLogId(Long cardLogId);

    List<CardLog> findByUser(User user);

}
