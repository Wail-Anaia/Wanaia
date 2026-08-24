package com.wanaia.domain.decision.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ScoreResultDto(
    Long id,
    String entityType,
    Long entityId,
    String scoreType,
    String algorithmVersion,
    BigDecimal scoreValue,
    String ratingClass,
    BigDecimal confidenceLevel,
    Instant calculatedAt,
    List<ScoreExplanationDto> explanations
) {}
