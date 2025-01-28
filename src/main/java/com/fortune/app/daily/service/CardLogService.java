package com.fortune.app.daily.service;

import com.fortune.app.card.entity.CardInterpretation;
import com.fortune.app.common.exception.CustomException;
import com.fortune.app.daily.dto.CardLogDto;
import com.fortune.app.daily.dto.CardLogRequestDto;
import com.fortune.app.daily.entity.CardLog;
import com.fortune.app.daily.repository.CardLogRepository;
import com.fortune.app.enums.ErrorCode;
import com.fortune.app.user.entity.User;
import com.fortune.app.card.repostiory.CardInterpretationRepository;
import com.fortune.app.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardLogService {

    private final CardLogRepository cardLogRepository;
    private final CardInterpretationRepository cardInterpretationRepository;
    private final UserRepository userRepository;

    public CardLogService(CardLogRepository cardLogRepository, CardInterpretationRepository cardInterpretationRepository, UserRepository userRepository) {
        this.cardLogRepository = cardLogRepository;
        this.cardInterpretationRepository = cardInterpretationRepository;
        this.userRepository = userRepository;
    }

    public CardLogDto createCardLog(CardLogRequestDto dto) {
        CardInterpretation ci = cardInterpretationRepository.findById(dto.getCardInterpretationId())
                .orElseThrow(() -> new RuntimeException("CardInterpretation not found"));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        CardLog cardLog = CardLog.builder()
                .cardInterpretation(ci)
                .user(user)
                .build();
        CardLog saved = cardLogRepository.save(cardLog);

        return CardLogDto.mapToDto(saved);
    }

    public CardLogDto getCardLog(Long cardLogId) {
        CardLog cardLog = cardLogRepository.findByCardLogId(cardLogId)
                .orElseThrow(() -> new RuntimeException("CardLog not found"));
        return CardLogDto.mapToDto(cardLog);
    }

    @Transactional
    public CardLogDto updateCardLog(Long cardLogId, CardLogRequestDto dto) {
        CardLog cardLog = cardLogRepository.findByCardLogId(cardLogId)
                .orElseThrow(() -> new RuntimeException("CardLog not found"));

        Optional.ofNullable(dto.getCardInterpretationId())
                .map(id -> cardInterpretationRepository.findByCardInterpretationId(id)
                        .orElseThrow(() -> new RuntimeException("CardInterpretation not found")))
                .ifPresent(cardLog::setCardInterpretation);

        Optional.ofNullable(dto.getUserId())
                .map(id -> userRepository.findByUserIdQueryDSL(id)
                        .orElseThrow(() -> new RuntimeException("User not found")))
                .ifPresent(cardLog::setUser);

        cardLog.setUpdatedAt(LocalDateTime.now());

        return CardLogDto.mapToDto(cardLog);
    }

    public void deleteCardLog(Long cardLogId) {
        if (!cardLogRepository.existsById(cardLogId)) {
            throw new RuntimeException("CardLog not found");
        }

        cardLogRepository.deleteById(cardLogId);
    }

    public List<CardLogDto> getCardLogList() {
        return cardLogRepository.findAll()
                .stream()
                .map(CardLogDto::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CardLog> getCardLogListByUserId(Long userId) {
        User user = userRepository.findByUserIdQueryDSL(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return cardLogRepository.findByUser(user);
    }
}
