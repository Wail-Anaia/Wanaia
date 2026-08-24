package com.wanaia.domain.decision.dto;

import java.time.Instant;
import java.util.List;

public record RecommendationTraceDto(
    Long id,
    Long userId,
    String marketCode,
    String algorithmVersion,
    String profileSnapshotJson,
    List<Long> rankedProductIds,
    Instant generatedAt
) {}
