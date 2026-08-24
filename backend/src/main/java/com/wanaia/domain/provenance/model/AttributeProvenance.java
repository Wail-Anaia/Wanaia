package com.wanaia.domain.provenance.model;

import com.wanaia.common.base.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Instant;

@Entity
@Table(name = "attribute_provenance", indexes = {
    @Index(name = "idx_attr_provenance_lookup", columnList = "entity_type, entity_id, attribute_name")
})
public class AttributeProvenance extends BaseEntity {

    @Column(name = "entity_type", nullable = false, length = 50)
    private String entityType; // e.g. "MOBILITY_PRODUCT", "POWERTRAIN", "MARKET_AVAILABILITY"

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Column(name = "attribute_name", nullable = false, length = 100)
    private String attributeName; // e.g. "wltp_consumption_metric", "total_power_hp"

    @Enumerated(EnumType.STRING)
    @Column(name = "epistemic_type", nullable = false, length = 30)
    private EpistemicType epistemicType;

    @Column(name = "source_id")
    private Long sourceId; // Reference to DataSource

    @Column(name = "source_reference", length = 255)
    private String sourceReference; // e.g. "Official Homologation Sheet #2026-MA-771"

    @Column(name = "collected_at", nullable = false)
    private Instant collectedAt = Instant.now();

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom = LocalDate.now();

    @Column(name = "valid_to")
    private LocalDate validTo;

    @Column(name = "market_code", nullable = false, length = 3)
    private String marketCode = "MAR";

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false, length = 30)
    private VerificationStatus verificationStatus = VerificationStatus.OFFICIALLY_VERIFIED;

    @Enumerated(EnumType.STRING)
    @Column(name = "confidence_level", nullable = false, length = 20)
    private ConfidenceLevel confidenceLevel = ConfidenceLevel.CONFIRMED_HIGH;

    public AttributeProvenance() {}

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

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public EpistemicType getEpistemicType() {
        return epistemicType;
    }

    public void setEpistemicType(EpistemicType epistemicType) {
        this.epistemicType = epistemicType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceReference() {
        return sourceReference;
    }

    public void setSourceReference(String sourceReference) {
        this.sourceReference = sourceReference;
    }

    public Instant getCollectedAt() {
        return collectedAt;
    }

    public void setCollectedAt(Instant collectedAt) {
        this.collectedAt = collectedAt;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public String getMarketCode() {
        return marketCode;
    }

    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public ConfidenceLevel getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(ConfidenceLevel confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }
}
