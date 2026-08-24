package com.wanaia.domain.decision.model;

import com.wanaia.common.base.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "score_results", indexes = {
    @Index(name = "idx_scores_lookup", columnList = "entity_type, entity_id, score_type")
})
public class ScoreResult extends BaseEntity {

    @Column(name = "entity_type", nullable = false, length = 50)
    private String entityType; // "MOBILITY_PRODUCT", "MARKETPLACE_LISTING"

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "score_type", nullable = false, length = 50)
    private ScoreType scoreType;

    @Column(name = "algorithm_version", nullable = false, length = 30)
    private String algorithmVersion; // e.g. "v1.0.0-CAR"

    @Column(name = "score_value", nullable = false, precision = 5, scale = 2)
    private BigDecimal scoreValue; // 0.00 to 100.00

    @Column(name = "rating_class", nullable = false, length = 30)
    private String ratingClass; // "EXCELLENT", "GOOD", "FAIR", "POOR"

    @Column(name = "confidence_level", nullable = false, precision = 3, scale = 2)
    private BigDecimal confidenceLevel = BigDecimal.valueOf(0.95); // 0.00 to 1.00

    @Column(name = "input_snapshot_id", nullable = false)
    private Long inputSnapshotId; // Reference to ScoreInputSnapshot

    @Column(name = "dimension_breakdown_json", columnDefinition = "TEXT")
    private String dimensionBreakdownJson; // JSON breakdown of dimension scores

    @Column(name = "calculated_at", nullable = false)
    private Instant calculatedAt = Instant.now();

    public ScoreResult() {}

    public ScoreResult(String entityType, Long entityId, ScoreType scoreType, String algorithmVersion,
                       BigDecimal scoreValue, String ratingClass, BigDecimal confidenceLevel, Long inputSnapshotId) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.scoreType = scoreType;
        this.algorithmVersion = algorithmVersion;
        this.scoreValue = scoreValue;
        this.ratingClass = ratingClass;
        this.confidenceLevel = confidenceLevel;
        this.inputSnapshotId = inputSnapshotId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public ScoreType getScoreType() {
        return scoreType;
    }

    public void setScoreType(ScoreType scoreType) {
        this.scoreType = scoreType;
    }

    public String getAlgorithmVersion() {
        return algorithmVersion;
    }

    public void setAlgorithmVersion(String algorithmVersion) {
        this.algorithmVersion = algorithmVersion;
    }

    public BigDecimal getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(BigDecimal scoreValue) {
        this.scoreValue = scoreValue;
    }

    public String getRatingClass() {
        return ratingClass;
    }

    public void setRatingClass(String ratingClass) {
        this.ratingClass = ratingClass;
    }

    public BigDecimal getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(BigDecimal confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }

    public Long getInputSnapshotId() {
        return inputSnapshotId;
    }

    public void setInputSnapshotId(Long inputSnapshotId) {
        this.inputSnapshotId = inputSnapshotId;
    }

    public String getDimensionBreakdownJson() {
        return dimensionBreakdownJson;
    }

    public void setDimensionBreakdownJson(String dimensionBreakdownJson) {
        this.dimensionBreakdownJson = dimensionBreakdownJson;
    }

    public Instant getCalculatedAt() {
        return calculatedAt;
    }

    public void setCalculatedAt(Instant calculatedAt) {
        this.calculatedAt = calculatedAt;
    }
}
