package com.fortune.app.card.controller;

import com.fortune.app.card.dto.CardInterpretationDto;
import com.fortune.app.card.dto.CardInterpretationRequestDto;
import com.fortune.app.card.service.CardInterpretationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cardInterpretation")
public class CardInterpretationController {

    private final CardInterpretationService cardInterpretationService;

    public CardInterpretationController(CardInterpretationService cardInterpretationService) {
        this.cardInterpretationService = cardInterpretationService;
    }

    @PostMapping
    public ResponseEntity<CardInterpretationDto> createCardInterpretation(@Valid @RequestBody CardInterpretationRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardInterpretationService.createCardInterpretation(dto));
    }

    @GetMapping("/{cardInterpretationId}")
    public ResponseEntity<CardInterpretationDto> getCardInterpretation(@PathVariable("cardInterpretationId") Long cardId) {
        return ResponseEntity.status(HttpStatus.OK).body(cardInterpretationService.getCardInterpretation(cardId));
    }

    @PutMapping("/{cardInterpretationId}")
    public ResponseEntity<CardInterpretationDto> updateCardInterpretation(@PathVariable("cardInterpretationId") Long cardId,
                                                                          @Valid @RequestBody CardInterpretationRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(cardInterpretationService.updateCardInterpretation(cardId, dto));
    }

    @DeleteMapping("/{cardInterpretationId}")
    public ResponseEntity<Void> deleteCardInterpretation(@PathVariable("cardInterpretationId") Long cardId) {
        cardInterpretationService.deleteCardInterpretation(cardId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<CardInterpretationDto>> getCardInterpretationList(@PathVariable("cardInterpretationId") Long cardId) {
        return ResponseEntity.status(HttpStatus.OK).body(cardInterpretationService.getCardInterpretationList());
    }

}
