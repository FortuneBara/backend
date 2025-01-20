package com.fortune.app.card.service;

import com.fortune.app.card.dto.FortuneDto;
import com.fortune.app.card.dto.FortuneRequestDto;
import com.fortune.app.card.entity.Fortune;
import com.fortune.app.card.repostiory.FortuneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FortuneService {

    private final FortuneRepository fortuneRepository;

    public FortuneService(FortuneRepository fortuneRepository) {
        this.fortuneRepository = fortuneRepository;
    }

    public FortuneDto createFortune(FortuneRequestDto dto) {
        Fortune fortune = Fortune.builder()
                .type(dto.getType())
                .build();
        Fortune saved = fortuneRepository.save(fortune);

        return FortuneDto.mapToDto(saved);
    }

    public FortuneDto getFortune(Long fortuneId) {
        Fortune fortune = fortuneRepository.findByFortuneId(fortuneId)
                .orElseThrow(() -> new RuntimeException("Fortune not found"));
        return FortuneDto.mapToDto(fortune);
    }

    @Transactional
    public FortuneDto updateFortune(Long fortuneId, FortuneRequestDto dto) {
        Fortune fortune = fortuneRepository.findByFortuneId(fortuneId)
                .orElseThrow(() -> new RuntimeException("Fortune not found"));

        Optional.ofNullable(dto.getType()).ifPresent(fortune::setType);

        fortune.setUpdatedAt(LocalDateTime.now());

        return FortuneDto.mapToDto(fortune);
    }

    public void deleteFortune(Long fortuneId) {
        if (!fortuneRepository.existsById(fortuneId)) throw new RuntimeException("Fortune not found");

        fortuneRepository.deleteById(fortuneId);
    }

    public List<FortuneDto> getFortuneList() {
        return fortuneRepository.findAll().stream().map(FortuneDto::mapToDto).toList();
    }
}
