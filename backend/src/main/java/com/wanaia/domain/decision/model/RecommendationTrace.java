package com.wanaia.domain.decision.model;

import com.wanaia.common.base.BaseEntity;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "recommendation_traces", indexes = {
    @Index(name = "idx_rec_traces_user", columnList = "user_id")
})
public class RecommendationTrace extends BaseEntity {

    @Column(name = "user_id")
    private Long userId; // Nullable for anonymous/guest sessions

    @Column(name = "market_code", nullable = false, length = 3)
    private String marketCode = "MAR";

    @Column(name = "algorithm_version", nullable = false, length = 30)
    private String algorithmVersion; // e.g. "v1.0.0-CAR"

    @Column(name = "profile_snapshot_json", nullable = false, columnDefinition = "TEXT")
    private String profileSnapshotJson;

    @Column(name = "candidate_product_ids_json", nullable = false, columnDefinition = "TEXT")
    private String candidateProductIdsJson;

    @Column(name = "ranked_product_ids_json", nullable = false, columnDefinition = "TEXT")
    private String rankedProductIdsJson;

    @Column(name = "scores_map_json", nullable = false, columnDefinition = "TEXT")
    private String scoresMapJson;

    @Column(name = "explanations_map_json", nullable = false, columnDefinition = "TEXT")
    private String explanationsMapJson;

    @Column(name = "generated_at", nullable = false)
    private Instant generatedAt = Instant.now();

    public RecommendationTrace() {}

    public RecommendationTrace(Long userId, String marketCode, String algorithmVersion, String profileSnapshotJson,
                               String candidateProductIdsJson, String rankedProductIdsJson,
                               String scoresMapJson, String explanationsMapJson) {
        this.userId = userId;
        this.marketCode = marketCode;
        this.algorithmVersion = algorithmVersion;
        this.profileSnapshotJson = profileSnapshotJson;
        this.candidateProductIdsJson = candidateProductIdsJson;
        this.rankedProductIdsJson = rankedProductIdsJson;
        this.scoresMapJson = scoresMapJson;
        this.explanationsMapJson = explanationsMapJson;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMarketCode() {
        return marketCode;
    }

    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

    public String getAlgorithmVersion() {
        return algorithmVersion;
    }

    public void setAlgorithmVersion(String algorithmVersion) {
        this.algorithmVersion = algorithmVersion;
    }

    public String getProfileSnapshotJson() {
        return profileSnapshotJson;
    }

    public void setProfileSnapshotJson(String profileSnapshotJson) {
        this.profileSnapshotJson = profileSnapshotJson;
    }

    public String getCandidateProductIdsJson() {
        return candidateProductIdsJson;
    }

    public void setCandidateProductIdsJson(String candidateProductIdsJson) {
        this.candidateProductIdsJson = candidateProductIdsJson;
    }

    public String getRankedProductIdsJson() {
        return rankedProductIdsJson;
    }

    public void setRankedProductIdsJson(String rankedProductIdsJson) {
        this.rankedProductIdsJson = rankedProductIdsJson;
    }

    public String getScoresMapJson() {
        return scoresMapJson;
    }

    public void setScoresMapJson(String scoresMapJson) {
        this.scoresMapJson = scoresMapJson;
    }

    public String getExplanationsMapJson() {
        return explanationsMapJson;
    }

    public void setExplanationsMapJson(String explanationsMapJson) {
        this.explanationsMapJson = explanationsMapJson;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }
}
