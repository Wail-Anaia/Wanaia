package com.wanaia.domain.mobility.model;

import com.wanaia.common.base.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "brands", uniqueConstraints = {
    @UniqueConstraint(name = "uq_brand_category_slug", columnNames = {"category_id", "slug"})
}, indexes = {
    @Index(name = "idx_brands_slug", columnList = "slug"),
    @Index(name = "idx_brands_category", columnList = "category_id")
})
public class Brand extends BaseEntity {

    @Column(name = "category_id", nullable = false)
    private Long categoryId; // Referenced by ID (DDD Aggregate Boundary)

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "slug", nullable = false, length = 100)
    private String slug;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "country_of_origin", length = 3)
    private String countryOfOrigin; // ISO-3166-1 alpha-3, e.g. "JPN", "FRA", "USA", "DEU"

    @Column(name = "founded_year")
    private Integer foundedYear;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "website_url", length = 500)
    private String websiteUrl;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    public Brand() {}

    public Brand(Long categoryId, String name, String slug, String logoUrl, String countryOfOrigin, Integer foundedYear) {
        this.categoryId = categoryId;
        this.name = name;
        this.slug = slug;
        this.logoUrl = logoUrl;
        this.countryOfOrigin = countryOfOrigin;
        this.foundedYear = foundedYear;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public Integer getFoundedYear() {
        return foundedYear;
    }

    public void setFoundedYear(Integer foundedYear) {
        this.foundedYear = foundedYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
