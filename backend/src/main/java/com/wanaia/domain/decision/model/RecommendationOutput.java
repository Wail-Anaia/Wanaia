package com.wanaia.domain.decision.model;

import java.time.Instant;
import java.util.List;

public record RecommendationOutput(
    Long userId,
    String marketCode,
    String algorithmVersion,
    int totalCandidatesEvaluated,
    int qualifiedCandidatesCount,
    List<RankedRecommendation> rankedRecommendations,
    String comparisonRationale,
    Instant generatedAt,
    String rawSnapshotJson,
    String snapshotHash
) {}
