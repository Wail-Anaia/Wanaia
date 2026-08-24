package com.wanaia.domain.mobility.model;

import com.wanaia.common.base.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "generations", uniqueConstraints = {
    @UniqueConstraint(name = "uq_gen_model_slug", columnNames = {"model_id", "slug"})
}, indexes = {
    @Index(name = "idx_generations_model", columnList = "model_id")
})
public class Generation extends BaseEntity {

    @Column(name = "model_id", nullable = false)
    private Long modelId; // Referenced by ID

    @Column(name = "name", nullable = false, length = 100)
    private String name; // e.g. "5th Generation (XA50)"

    @Column(name = "slug", nullable = false, length = 150)
    private String slug;

    @Column(name = "internal_platform_code", length = 50)
    private String internalPlatformCode; // e.g. "XA50", "G20"

    @Column(name = "start_year", nullable = false)
    private Integer startYear;

    @Column(name = "end_year")
    private Integer endYear; // NULL if currently in active production

    @Column(name = "hero_image_url", length = 500)
    private String heroImageUrl;

    @Column(name = "is_current", nullable = false)
    private Boolean isCurrent = true;

    public Generation() {}

    public Generation(Long modelId, String name, String slug, String internalPlatformCode, Integer startYear, Integer endYear, String heroImageUrl, Boolean isCurrent) {
        this.modelId = modelId;
        this.name = name;
        this.slug = slug;
        this.internalPlatformCode = internalPlatformCode;
        this.startYear = startYear;
        this.endYear = endYear;
        this.heroImageUrl = heroImageUrl;
        this.isCurrent = isCurrent;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
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

    public String getInternalPlatformCode() {
        return internalPlatformCode;
    }

    public void setInternalPlatformCode(String internalPlatformCode) {
        this.internalPlatformCode = internalPlatformCode;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public String getHeroImageUrl() {
        return heroImageUrl;
    }

    public void setHeroImageUrl(String heroImageUrl) {
        this.heroImageUrl = heroImageUrl;
    }

    public Boolean getCurrent() {
        return isCurrent;
    }

    public void setCurrent(Boolean current) {
        isCurrent = current;
    }
}
