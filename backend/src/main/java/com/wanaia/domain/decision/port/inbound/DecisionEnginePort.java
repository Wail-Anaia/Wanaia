package com.wanaia.domain.decision.port.inbound;

import com.wanaia.domain.decision.dto.RecommendationTraceDto;
import com.wanaia.domain.decision.dto.ScoreResultDto;

import java.util.List;
import java.util.Optional;

public interface DecisionEnginePort {
    Optional<ScoreResultDto> getLatestScore(String entityType, Long entityId, String scoreType);
    List<ScoreResultDto> getAllScoresForEntity(String entityType, Long entityId);
    List<RecommendationTraceDto> getRecommendationTracesForUser(Long userId);
}
