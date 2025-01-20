package com.fortune.app.card.service;

import com.fortune.app.card.dto.CardDto;
import com.fortune.app.card.entity.Card;
import com.fortune.app.card.dto.CardRequestDto;
import com.fortune.app.card.repostiory.CardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public CardDto createCard(CardRequestDto dto) {
        Card card = Card.builder()
                .name(dto.getName())
                .orientation(dto.getOrientation())
                .build();
        Card saved = cardRepository.save(card);

        return CardDto.mapToDto(saved);
    }

    public CardDto getCard(Long cardId) {
        Card card = cardRepository.findByCardId(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        return CardDto.mapToDto(card);
    }

    @Transactional
    public CardDto updateCard(Long cardId, CardRequestDto dto) {
        Card card = cardRepository.findByCardId(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        Optional.ofNullable(dto.getName()).ifPresent(card::setName);
        Optional.ofNullable(dto.getOrientation()).ifPresent(card::setOrientation);

        card.setUpdatedAt(LocalDateTime.now());

        return CardDto.mapToDto(card);
    }

    public void deleteCard(Long cardId) {
        if (!cardRepository.existsById(cardId)) throw new RuntimeException("Card not found");

        cardRepository.deleteById(cardId);
    }

    public List<CardDto> getCardList() {
        return cardRepository.findAll()
                .stream()
                .map(CardDto::mapToDto)
                .toList();
    }
}
