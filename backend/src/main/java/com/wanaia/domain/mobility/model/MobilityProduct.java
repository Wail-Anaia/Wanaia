package com.wanaia.domain.mobility.model;

import com.wanaia.common.base.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "mobility_products", uniqueConstraints = {
    @UniqueConstraint(name = "uq_product_gen_slug", columnNames = {"generation_id", "slug"})
}, indexes = {
    @Index(name = "idx_products_generation", columnList = "generation_id"),
    @Index(name = "idx_products_slug", columnList = "slug"),
    @Index(name = "idx_products_uuid", columnList = "uuid")
})
public class MobilityProduct extends BaseEntity {

    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    private UUID uuid = UUID.randomUUID();

    @Column(name = "generation_id", nullable = false)
    private Long generationId; // Referenced by ID (DDD boundary)

    @Column(name = "variant_name", nullable = false, length = 150)
    private String variantName; // e.g. "2.5 Hybrid AWD-i", "TCe 90", "Long Range Dual Motor"

    @Column(name = "slug", nullable = false, length = 200)
    private String slug;

    @Column(name = "body_style", nullable = false, length = 50)
    private String bodyStyle; // "SUV", "SEDAN", "HATCHBACK", "NAKED", "PANEL_VAN"

    @Column(name = "powertrain_config_id", nullable = false)
    private Long powertrainConfigId; // Referenced by ID

    // Common Physical Dimensions & Attributes
    @Column(name = "curb_weight_kg")
    private Integer curbWeightKg;

    @Column(name = "length_mm")
    private Integer lengthMm;

    @Column(name = "width_mm")
    private Integer widthMm;

    @Column(name = "height_mm")
    private Integer heightMm;

    @Column(name = "wheelbase_mm")
    private Integer wheelbaseMm;

    @Column(name = "boot_capacity_liters")
    private Integer bootCapacityLiters;

    @Column(name = "seat_count")
    private Integer seatCount = 5;

    @Column(name = "safety_rating_ncap", precision = 3, scale = 1)
    private BigDecimal safetyRatingNcap; // 0.0 to 5.0

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public MobilityProduct() {}

    public MobilityProduct(Long generationId, String variantName, String slug, String bodyStyle, Long powertrainConfigId, Integer seatCount) {
        this.generationId = generationId;
        this.variantName = variantName;
        this.slug = slug;
        this.bodyStyle = bodyStyle;
        this.powertrainConfigId = powertrainConfigId;
        this.seatCount = seatCount;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Long getGenerationId() {
        return generationId;
    }

    public void setGenerationId(Long generationId) {
        this.generationId = generationId;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getBodyStyle() {
        return bodyStyle;
    }

    public void setBodyStyle(String bodyStyle) {
        this.bodyStyle = bodyStyle;
    }

    public Long getPowertrainConfigId() {
        return powertrainConfigId;
    }

    public void setPowertrainConfigId(Long powertrainConfigId) {
        this.powertrainConfigId = powertrainConfigId;
    }

    public Integer getCurbWeightKg() {
        return curbWeightKg;
    }

    public void setCurbWeightKg(Integer curbWeightKg) {
        this.curbWeightKg = curbWeightKg;
    }

    public Integer getLengthMm() {
        return lengthMm;
    }

    public void setLengthMm(Integer lengthMm) {
        this.lengthMm = lengthMm;
    }

    public Integer getWidthMm() {
        return widthMm;
    }

    public void setWidthMm(Integer widthMm) {
        this.widthMm = widthMm;
    }

    public Integer getHeightMm() {
        return heightMm;
    }

    public void setHeightMm(Integer heightMm) {
        this.heightMm = heightMm;
    }

    public Integer getWheelbaseMm() {
        return wheelbaseMm;
    }

    public void setWheelbaseMm(Integer wheelbaseMm) {
        this.wheelbaseMm = wheelbaseMm;
    }

    public Integer getBootCapacityLiters() {
        return bootCapacityLiters;
    }

    public void setBootCapacityLiters(Integer bootCapacityLiters) {
        this.bootCapacityLiters = bootCapacityLiters;
    }

    public Integer getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(Integer seatCount) {
        this.seatCount = seatCount;
    }

    public BigDecimal getSafetyRatingNcap() {
        return safetyRatingNcap;
    }

    public void setSafetyRatingNcap(BigDecimal safetyRatingNcap) {
        this.safetyRatingNcap = safetyRatingNcap;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
