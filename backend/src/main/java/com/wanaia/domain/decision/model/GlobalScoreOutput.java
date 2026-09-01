package com.wanaia.domain.decision.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record GlobalScoreOutput(
    Long productId,
    String algorithmVersion,
    BigDecimal scoreValue,       // 0.00 - 100.00
    String ratingClass,          // EXCELLENT, GOOD, FAIR, POOR
    BigDecimal confidenceLevel,  // 0.00 - 1.00
    Map<String, BigDecimal> dimensionScores, // Reliability, Safety, Efficiency, Value, Practicality, Performance, Resale
    List<ScoreExplanationItem> explanations,
    String rawSnapshotJson,
    String snapshotHash
) {}
