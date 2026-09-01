package com.wanaia.domain.decision.model;

import java.math.BigDecimal;
import java.util.List;

public record RankedRecommendation(
    Long productId,
    String brandName,
    String modelName,
    String variantName,
    int rank,
    BigDecimal blendedScore,
    BigDecimal personalFitScore,
    BigDecimal globalScore,
    BigDecimal fiveYearTco,
    BigDecimal monthlyTco,
    boolean meetsHardConstraints,
    List<String> violatedConstraints,
    List<ScoreExplanationItem> reasonsWhyRecommended,
    List<ScoreExplanationItem> tradeOffs
) {}
