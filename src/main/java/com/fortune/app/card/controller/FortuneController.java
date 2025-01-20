package com.fortune.app.card.controller;

import com.fortune.app.card.dto.FortuneDto;
import com.fortune.app.card.dto.FortuneRequestDto;
import com.fortune.app.card.service.FortuneService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fortune")
public class FortuneController {

    private final FortuneService fortuneService;

    public FortuneController(FortuneService fortuneService) {
        this.fortuneService = fortuneService;
    }

    @PostMapping
    public ResponseEntity<FortuneDto> createFortune(@Valid @RequestBody FortuneRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fortuneService.createFortune(dto));
    }

    @GetMapping("/{fortuneId}")
    public ResponseEntity<FortuneDto> getFortune(@PathVariable("fortuneId") Long fortuneId) {
        return ResponseEntity.status(HttpStatus.OK).body(fortuneService.getFortune(fortuneId));
    }

    @PatchMapping("/{fortuneId}")
    public ResponseEntity<FortuneDto> updateFortune(@PathVariable("fortuneId") Long fortuneId,
                                                    @Valid @RequestBody FortuneRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(fortuneService.updateFortune(fortuneId, dto));
    }

    @DeleteMapping("/{fortuneId}")
    public ResponseEntity<Void> deleteFortune(@PathVariable("fortuneId") Long fortuneId, @RequestBody FortuneRequestDto dto) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<FortuneDto>> getFortuneList() {
        return ResponseEntity.status(HttpStatus.OK).body(fortuneService.getFortuneList());
    }
}
