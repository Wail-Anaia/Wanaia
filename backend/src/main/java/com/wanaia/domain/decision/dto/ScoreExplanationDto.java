package com.wanaia.domain.decision.dto;

public record ScoreExplanationDto(
    Long id,
    String type,
    String category,
    String code,
    String messageTemplate,
    String provenanceRef
) {}
