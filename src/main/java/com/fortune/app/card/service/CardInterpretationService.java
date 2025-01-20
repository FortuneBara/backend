package com.fortune.app.card.service;

import com.fortune.app.card.dto.CardInterpretationRequestDto;
import com.fortune.app.card.repostiory.CardRepository;
import com.fortune.app.card.entity.Fortune;
import com.fortune.app.card.repostiory.FortuneRepository;
import com.fortune.app.card.dto.CardInterpretationDto;
import com.fortune.app.card.entity.Card;
import com.fortune.app.card.entity.CardInterpretation;
import com.fortune.app.card.repostiory.CardInterpretationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CardInterpretationService {

    private final CardInterpretationRepository cardInterpretationRepository;
    private final CardRepository cardRepository;
    private final FortuneRepository fortuneRepository;

    public CardInterpretationService(CardInterpretationRepository cardInterpretationRepository, CardRepository cardRepository, FortuneRepository fortuneRepository) {
        this.cardInterpretationRepository = cardInterpretationRepository;
        this.cardRepository = cardRepository;
        this.fortuneRepository = fortuneRepository;
    }

    public CardInterpretationDto createCardInterpretation(CardInterpretationRequestDto dto) {
        Card card = cardRepository.findByCardId(dto.getCardId())
                .orElseThrow(() -> new RuntimeException("Card not found"));
        Fortune fortune = fortuneRepository.findByFortuneId(dto.getFortuneId())
                .orElseThrow(() -> new RuntimeException("Fortune not found"));

        CardInterpretation cardInterpretation = CardInterpretation.builder()
                .card(card)
                .fortune(fortune)
                .interpretationContent(dto.getInterpretationContent())
                .build();
        CardInterpretation saved = cardInterpretationRepository.save(cardInterpretation);

        return CardInterpretationDto.mapToDto(saved);
    }

    public CardInterpretationDto getCardInterpretation(Long interpretationId) {
        CardInterpretation entity = cardInterpretationRepository.findByCardInterpretationId(interpretationId)
                .orElseThrow(() -> new RuntimeException("Interpretation not found"));
        return CardInterpretationDto.mapToDto(entity);
    }

    @Transactional
    public CardInterpretationDto updateCardInterpretation(Long interpretationId, CardInterpretationRequestDto dto) {
        CardInterpretation cardInterpretation = cardInterpretationRepository.findByCardInterpretationId(interpretationId)
                .orElseThrow(() -> new RuntimeException("Interpretation not found"));

        Optional.ofNullable(dto.getCardId()).map(id -> cardRepository.findByCardId(id)
                        .orElseThrow(() -> new RuntimeException("Card not found")))
                .ifPresent(cardInterpretation::setCard);

        Optional.ofNullable(dto.getFortuneId()).map(id -> fortuneRepository.findByFortuneId(id)
                        .orElseThrow(() -> new RuntimeException("Fortune not found")))
                .ifPresent(cardInterpretation::setFortune);

        Optional.ofNullable(dto.getInterpretationContent()).ifPresent(cardInterpretation::setInterpretationContent);

        cardInterpretation.setUpdatedAt(LocalDateTime.now());

        return CardInterpretationDto.mapToDto(cardInterpretation);
    }

    public void deleteCardInterpretation(Long interpretationId) {
        if (!cardInterpretationRepository.existsById(interpretationId))
            throw new RuntimeException("Interpretation not found");
        cardInterpretationRepository.deleteById(interpretationId);
    }

    public List<CardInterpretationDto> getCardInterpretationList() {
        return cardInterpretationRepository.findAll().stream().map(CardInterpretationDto::mapToDto).toList();
    }
}
