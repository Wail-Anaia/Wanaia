package com.wanaia.domain.decision.controller;

import com.wanaia.common.api.ApiResponse;
import com.wanaia.domain.decision.dto.*;
import com.wanaia.domain.decision.model.*;
import com.wanaia.domain.decision.service.DecisionEngineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/decisions")
@Tag(name = "Decision & Intelligence Engine", description = "Deterministic Global WANAIA Score, Personal Fit Score, Deal Score, TCO, and Ranked Recommendations.")
public class DecisionEngineController {

    private final DecisionEngineService decisionEngineService;

    public DecisionEngineController(DecisionEngineService decisionEngineService) {
        this.decisionEngineService = decisionEngineService;
    }

    @GetMapping("/global-score/{productId}")
    @Operation(summary = "Calculate and retrieve deterministic Global WANAIA Score V1 (7 weighted dimensions)")
    public ResponseEntity<ApiResponse<GlobalScoreOutput>> getGlobalScore(@PathVariable Long productId) {
        GlobalScoreOutput output = decisionEngineService.calculateAndPersistGlobalScore(productId);
        return ResponseEntity.ok(ApiResponse.success(output));
    }

    @GetMapping("/personal-fit/{productId}")
    @Operation(summary = "Calculate Personal Fit Score V1 matching vehicle against user mobility profile")
    public ResponseEntity<ApiResponse<PersonalFitScoreOutput>> getPersonalFitScore(
        @PathVariable Long productId,
        @RequestParam(defaultValue = "1") Long userId
    ) {
        PersonalFitScoreOutput output = decisionEngineService.calculatePersonalFitScore(productId, userId);
        return ResponseEntity.ok(ApiResponse.success(output));
    }

    @PostMapping("/deal-score")
    @Operation(summary = "Calculate Deal Score V1 against market valuation benchmarks")
    public ResponseEntity<ApiResponse<DealScoreOutput>> calculateDealScore(@RequestBody DealScoreInput input) {
        DealScoreOutput output = decisionEngineService.calculateDealScore(input);
        return ResponseEntity.ok(ApiResponse.success(output));
    }

    @GetMapping("/tco/{productId}")
    @Operation(summary = "Calculate Total Cost of Ownership (TCO) across 1, 3, and 5-year horizons")
    public ResponseEntity<ApiResponse<TcoOutput>> getTco(
        @PathVariable Long productId,
        @RequestParam(defaultValue = "MAR") String marketCode
    ) {
        TcoOutput output = decisionEngineService.calculateTco(productId, marketCode);
        return ResponseEntity.ok(ApiResponse.success(output));
    }

    @GetMapping("/recommendations")
    @Operation(summary = "Generate ranked vehicle recommendations with comparison rationale and decision trace")
    public ResponseEntity<ApiResponse<RecommendationOutput>> getRecommendations(
        @RequestParam(required = false, defaultValue = "1") Long userId,
        @RequestParam(required = false, defaultValue = "MAR") String marketCode
    ) {
        RecommendationOutput output = decisionEngineService.generateRecommendations(userId, marketCode);
        return ResponseEntity.ok(ApiResponse.success(output));
    }

    @GetMapping("/traces/user/{userId}")
    @Operation(summary = "Retrieve audit decision traces for user recommendations")
    public ResponseEntity<ApiResponse<List<RecommendationTraceDto>>> getRecommendationTraces(@PathVariable Long userId) {
        List<RecommendationTraceDto> traces = decisionEngineService.getRecommendationTracesForUser(userId);
        return ResponseEntity.ok(ApiResponse.success(traces));
    }

    @GetMapping("/scores/{entityType}/{entityId}")
    @Operation(summary = "Get historical calculated score results for an entity")
    public ResponseEntity<ApiResponse<List<ScoreResultDto>>> getScoresForEntity(
        @PathVariable String entityType,
        @PathVariable Long entityId
    ) {
        List<ScoreResultDto> scores = decisionEngineService.getAllScoresForEntity(entityType, entityId);
        return ResponseEntity.ok(ApiResponse.success(scores));
    }
}
