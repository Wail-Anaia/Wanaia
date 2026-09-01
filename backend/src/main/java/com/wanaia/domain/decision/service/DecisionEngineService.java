package com.wanaia.domain.decision.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanaia.common.exception.ResourceNotFoundException;
import com.wanaia.domain.decision.calculator.*;
import com.wanaia.domain.decision.dto.*;
import com.wanaia.domain.decision.model.*;
import com.wanaia.domain.decision.port.inbound.DecisionEnginePort;
import com.wanaia.domain.decision.repository.*;
import com.wanaia.domain.mobility.dto.MobilityProductDetailDto;
import com.wanaia.domain.mobility.port.inbound.MobilityProductQueryPort;
import com.wanaia.domain.profile.model.MobilityProfile;
import com.wanaia.domain.profile.repository.MobilityProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DecisionEngineService implements DecisionEnginePort {

    private final GlobalScoreCalculatorV1 globalScoreCalculator;
    private final PersonalFitScoreCalculatorV1 personalFitCalculator;
    private final DealScoreCalculatorV1 dealScoreCalculator;
    private final TcoCalculatorV1 tcoCalculator;
    private final RecommendationEngineV1 recommendationEngine;
    private final MobilityProductQueryPort mobilityProductQueryPort;
    private final MobilityProfileRepository mobilityProfileRepository;
    private final ScoreInputSnapshotRepository snapshotRepository;
    private final ScoreResultRepository scoreResultRepository;
    private final ScoreExplanationItemRepository explanationRepository;
    private final RecommendationTraceRepository traceRepository;
    private final ObjectMapper objectMapper;

    public DecisionEngineService(
        GlobalScoreCalculatorV1 globalScoreCalculator,
        PersonalFitScoreCalculatorV1 personalFitCalculator,
        DealScoreCalculatorV1 dealScoreCalculator,
        TcoCalculatorV1 tcoCalculator,
        RecommendationEngineV1 recommendationEngine,
        MobilityProductQueryPort mobilityProductQueryPort,
        MobilityProfileRepository mobilityProfileRepository,
        ScoreInputSnapshotRepository snapshotRepository,
        ScoreResultRepository scoreResultRepository,
        ScoreExplanationItemRepository explanationRepository,
        RecommendationTraceRepository traceRepository,
        ObjectMapper objectMapper
    ) {
        this.globalScoreCalculator = globalScoreCalculator;
        this.personalFitCalculator = personalFitCalculator;
        this.dealScoreCalculator = dealScoreCalculator;
        this.tcoCalculator = tcoCalculator;
        this.recommendationEngine = recommendationEngine;
        this.mobilityProductQueryPort = mobilityProductQueryPort;
        this.mobilityProfileRepository = mobilityProfileRepository;
        this.snapshotRepository = snapshotRepository;
        this.scoreResultRepository = scoreResultRepository;
        this.explanationRepository = explanationRepository;
        this.traceRepository = traceRepository;
        this.objectMapper = objectMapper;
    }

    public GlobalScoreOutput calculateAndPersistGlobalScore(Long productId) {
        MobilityProductDetailDto product = mobilityProductQueryPort.getProductDetailById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("MobilityProduct", "id", productId));

        Double consumption = (product.powertrain() != null && product.powertrain().wltpConsumptionMetric() != null)
            ? product.powertrain().wltpConsumptionMetric().doubleValue()
            : 6.0;

        Double accel = (product.powertrain() != null && product.powertrain().acceleration0100S() != null)
            ? product.powertrain().acceleration0100S().doubleValue()
            : 9.0;

        Double ncap = (product.safetyRatingNcap() != null) ? product.safetyRatingNcap().doubleValue() : null;

        GlobalScoreInput input = new GlobalScoreInput(
            product.id(),
            product.variantName(),
            product.bodyStyle(),
            (product.powertrain() != null) ? product.powertrain().propulsionType() : "ICE",
            BigDecimal.valueOf(350000.00),
            ncap,
            85.0,
            consumption,
            (product.powertrain() != null) ? product.powertrain().combinedPowerHp() : 120,
            accel,
            product.bootCapacityLiters(),
            product.seatCount(),
            0.68,
            null
        );

        GlobalScoreOutput output = globalScoreCalculator.calculate(input);

        // Persist Snapshot
        ScoreInputSnapshot snapshot = snapshotRepository.findBySnapshotHash(output.snapshotHash())
            .orElseGet(() -> snapshotRepository.save(new ScoreInputSnapshot(output.snapshotHash(), output.rawSnapshotJson())));

        // Persist Score Result
        ScoreResult result = new ScoreResult(
            "MOBILITY_PRODUCT",
            productId,
            ScoreType.GLOBAL_WANAIA,
            output.algorithmVersion(),
            output.scoreValue(),
            output.ratingClass(),
            output.confidenceLevel(),
            snapshot.getId()
        );
        ScoreResult savedResult = scoreResultRepository.save(result);

        // Persist Explanations
        for (ScoreExplanationItem item : output.explanations()) {
            item.setScoreResultId(savedResult.getId());
            explanationRepository.save(item);
        }

        return output;
    }

    public PersonalFitScoreOutput calculatePersonalFitScore(Long productId, Long userId) {
        MobilityProductDetailDto product = mobilityProductQueryPort.getProductDetailById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("MobilityProduct", "id", productId));

        MobilityProfile profile = mobilityProfileRepository.findByUserId(userId)
            .orElseGet(() -> {
                MobilityProfile p = new MobilityProfile(userId, "MA", "MAD", BigDecimal.valueOf(450000.00));
                p.setTypicalPassengerCount(4);
                p.setPriorityReliability(5);
                p.setPriorityFuelEconomy(4);
                return p;
            });

        Double consumption = (product.powertrain() != null && product.powertrain().wltpConsumptionMetric() != null)
            ? product.powertrain().wltpConsumptionMetric().doubleValue()
            : 6.0;

        Double accel = (product.powertrain() != null && product.powertrain().acceleration0100S() != null)
            ? product.powertrain().acceleration0100S().doubleValue()
            : 9.0;

        PersonalFitScoreInput input = new PersonalFitScoreInput(
            product.id(),
            product.variantName(),
            product.bodyStyle(),
            (product.powertrain() != null) ? product.powertrain().propulsionType() : "ICE",
            BigDecimal.valueOf(350000.00),
            consumption,
            product.seatCount(),
            2,
            product.bootCapacityLiters(),
            85.0,
            accel,
            profile
        );

        return personalFitCalculator.calculate(input);
    }

    public DealScoreOutput calculateDealScore(DealScoreInput input) {
        return dealScoreCalculator.calculate(input);
    }

    public TcoOutput calculateTco(Long productId, String marketCode) {
        MobilityProductDetailDto product = mobilityProductQueryPort.getProductDetailById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("MobilityProduct", "id", productId));

        MarketContext market = "FRA".equalsIgnoreCase(marketCode) ? MarketContext.france() : MarketContext.morocco();

        Double consumption = (product.powertrain() != null && product.powertrain().wltpConsumptionMetric() != null)
            ? product.powertrain().wltpConsumptionMetric().doubleValue()
            : 5.7;

        TcoInput input = new TcoInput(
            product.id(),
            BigDecimal.valueOf(350000.00),
            15000,
            consumption,
            (product.powertrain() != null) ? product.powertrain().propulsionType() : "HEV",
            9,
            BigDecimal.valueOf(3000.00),
            market
        );

        return tcoCalculator.calculate(input);
    }

    public RecommendationOutput generateRecommendations(Long userId, String marketCode) {
        MobilityProfile profile = (userId != null)
            ? mobilityProfileRepository.findByUserId(userId).orElseGet(() -> new MobilityProfile(userId, "MA", "MAD", BigDecimal.valueOf(500000.00)))
            : new MobilityProfile(0L, "MA", "MAD", BigDecimal.valueOf(500000.00));

        MarketContext market = "FRA".equalsIgnoreCase(marketCode) ? MarketContext.france() : MarketContext.morocco();

        // Query catalog candidates
        List<MobilityProductDetailDto> candidates = new ArrayList<>();
        mobilityProductQueryPort.getProductDetailById(1L).ifPresent(candidates::add);
        mobilityProductQueryPort.getProductDetailById(2L).ifPresent(candidates::add);
        mobilityProductQueryPort.getProductDetailById(3L).ifPresent(candidates::add);

        RecommendationInput input = new RecommendationInput(userId, profile, market, candidates);
        RecommendationOutput output = recommendationEngine.evaluateAndRank(input);

        // Persist Recommendation Trace
        List<Long> rankedIds = output.rankedRecommendations().stream().map(RankedRecommendation::productId).toList();
        String rankedIdsJson;
        try {
            rankedIdsJson = objectMapper.writeValueAsString(rankedIds);
        } catch (Exception e) {
            rankedIdsJson = "[]";
        }

        RecommendationTrace trace = new RecommendationTrace(
            userId,
            market.marketCode(),
            output.algorithmVersion(),
            output.rawSnapshotJson(),
            rankedIdsJson,
            rankedIdsJson,
            "{}",
            output.comparisonRationale()
        );
        traceRepository.save(trace);

        return output;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ScoreResultDto> getLatestScore(String entityType, Long entityId, String scoreType) {
        ScoreType type;
        try {
            type = ScoreType.valueOf(scoreType);
        } catch (Exception e) {
            return Optional.empty();
        }

        return scoreResultRepository.findFirstByEntityTypeAndEntityIdAndScoreTypeOrderByCalculatedAtDesc(entityType, entityId, type)
            .map(this::toScoreDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScoreResultDto> getAllScoresForEntity(String entityType, Long entityId) {
        return scoreResultRepository.findByEntityTypeAndEntityId(entityType, entityId)
            .stream()
            .map(this::toScoreDto)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecommendationTraceDto> getRecommendationTracesForUser(Long userId) {
        return traceRepository.findByUserIdOrderByGeneratedAtDesc(userId)
            .stream()
            .map(t -> new RecommendationTraceDto(
                t.getId(),
                t.getUserId(),
                t.getMarketCode(),
                t.getAlgorithmVersion(),
                t.getProfileSnapshotJson(),
                List.of(),
                t.getGeneratedAt()
            ))
            .toList();
    }

    private ScoreResultDto toScoreDto(ScoreResult entity) {
        List<ScoreExplanationDto> explanations = explanationRepository.findByScoreResultId(entity.getId())
            .stream()
            .map(e -> new ScoreExplanationDto(
                e.getId(),
                e.getType().name(),
                e.getCategory(),
                e.getCode(),
                e.getMessageTemplate(),
                e.getProvenanceRef()
            ))
            .toList();

        return new ScoreResultDto(
            entity.getId(),
            entity.getEntityType(),
            entity.getEntityId(),
            entity.getScoreType().name(),
            entity.getAlgorithmVersion(),
            entity.getScoreValue(),
            entity.getRatingClass(),
            entity.getConfidenceLevel(),
            entity.getCalculatedAt(),
            explanations
        );
    }
}
