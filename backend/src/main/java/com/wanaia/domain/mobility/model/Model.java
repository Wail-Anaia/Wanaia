package com.wanaia.domain.mobility.model;

import com.wanaia.common.base.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "models", uniqueConstraints = {
    @UniqueConstraint(name = "uq_model_brand_slug", columnNames = {"brand_id", "slug"})
}, indexes = {
    @Index(name = "idx_models_brand", columnList = "brand_id"),
    @Index(name = "idx_models_slug", columnList = "slug")
})
public class Model extends BaseEntity {

    @Column(name = "brand_id", nullable = false)
    private Long brandId; // Referenced by ID

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "slug", nullable = false, length = 150)
    private String slug;

    @Column(name = "segment_code", length = 50)
    private String segmentCode; // e.g. "C_SUV", "B_HATCHBACK", "NAKED_MIDWEIGHT"

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public Model() {}

    public Model(Long brandId, String name, String slug, String segmentCode) {
        this.brandId = brandId;
        this.name = name;
        this.slug = slug;
        this.segmentCode = segmentCode;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSegmentCode() {
        return segmentCode;
    }

    public void setSegmentCode(String segmentCode) {
        this.segmentCode = segmentCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
