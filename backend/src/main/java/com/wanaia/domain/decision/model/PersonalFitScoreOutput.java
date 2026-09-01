package com.wanaia.domain.decision.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record PersonalFitScoreOutput(
    Long productId,
    String algorithmVersion,
    BigDecimal fitScoreValue,    // 0.00 - 100.00
    boolean meetsHardConstraints,
    List<String> violatedHardConstraints,
    Map<String, BigDecimal> preferenceMatchScores,
    List<ScoreExplanationItem> reasonsWhyFits,
    List<ScoreExplanationItem> reasonsWhyDoesNotFit,
    String rawSnapshotJson,
    String snapshotHash
) {}
