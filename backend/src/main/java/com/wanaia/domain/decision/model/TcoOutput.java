package com.wanaia.domain.decision.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record TcoOutput(
    Long productId,
    String algorithmVersion,
    String currencyCode,
    Map<Integer, TcoHorizonBreakdown> horizons, // Key: 1, 3, 5 years
    List<ScoreExplanationItem> explanations,
    String rawSnapshotJson,
    String snapshotHash
) {
    public record TcoHorizonBreakdown(
        int horizonYears,
        BigDecimal totalTco,
        BigDecimal monthlyAverageCost,
        BigDecimal costPerKm,
        BigDecimal depreciationCost,
        BigDecimal energyCost,
        BigDecimal maintenanceCost,
        BigDecimal insuranceCost,
        BigDecimal taxesCost,
        BigDecimal financingCost,
        BigDecimal estimatedResidualValue
    ) {}
}
