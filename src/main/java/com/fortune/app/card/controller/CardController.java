package com.fortune.app.card.controller;

import com.fortune.app.card.dto.CardDto;
import com.fortune.app.card.dto.CardRequestDto;
import com.fortune.app.card.service.CardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/card")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    public ResponseEntity<CardDto> createCard(@Valid @RequestBody CardRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.createCard(dto));
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<CardDto> getCard(@PathVariable("cardId") Long cardId) {
        return ResponseEntity.status(HttpStatus.OK).body(cardService.getCard(cardId));
    }

    @PatchMapping("/{cardId}")
    public ResponseEntity<CardDto> updateCard(@PathVariable("cardId") Long cardId,
                                              @Valid @RequestBody CardRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(cardService.updateCard(cardId, dto));
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable("cardId") Long cardId) {
        cardService.deleteCard(cardId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<CardDto>> getCardList() {
        return ResponseEntity.status(HttpStatus.OK).body(cardService.getCardList());
    }
}
