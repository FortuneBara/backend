package com.fortune.app.daily.controller;

import com.fortune.app.daily.dto.CardLogDto;
import com.fortune.app.daily.dto.CardLogRequestDto;
import com.fortune.app.daily.service.CardLogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cardLog")
public class CardLogController {

    private final CardLogService cardLogService;

    public CardLogController(CardLogService cardLogService) {
        this.cardLogService = cardLogService;
    }

    @PostMapping
    public ResponseEntity<CardLogDto> createCardLog(@Valid @RequestBody CardLogRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardLogService.createCardLog(dto));
    }

    @GetMapping("/{cardLogId}")
    public ResponseEntity<CardLogDto> getCardLog(@PathVariable("cardLogId") Long cardId) {
        return ResponseEntity.status(HttpStatus.OK).body(cardLogService.getCardLog(cardId));
    }

    @PatchMapping("/{cardLogId}")
    public ResponseEntity<CardLogDto> updateCardLog(@PathVariable("cardLogId") Long cardId,
                                                    @Valid @RequestBody CardLogRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(cardLogService.updateCardLog(cardId, dto));
    }

    @DeleteMapping("/{cardLogId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long cardLogId) {
        cardLogService.deleteCardLog(cardLogId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<CardLogDto>> getCardLogList() {
        return ResponseEntity.status(HttpStatus.OK).body(cardLogService.getCardLogList());
    }
}
