package com.wanaia.domain.provenance.model;

import com.wanaia.common.base.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "data_sources")
public class DataSource extends BaseEntity {

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "source_type", nullable = false, length = 50)
    private String sourceType; // 'MANUFACTURER', 'REGULATOR', 'INDEPENDENT_TEST', 'DEALER', 'COMMUNITY'

    @Column(name = "trust_tier", nullable = false)
    private Integer trustTier = 1;

    @Column(name = "website_url", length = 500)
    private String websiteUrl;

    public DataSource() {}

    public DataSource(String code, String name, String sourceType, Integer trustTier, String websiteUrl) {
        this.code = code;
        this.name = name;
        this.sourceType = sourceType;
        this.trustTier = trustTier;
        this.websiteUrl = websiteUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Integer getTrustTier() {
        return trustTier;
    }

    public void setTrustTier(Integer trustTier) {
        this.trustTier = trustTier;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }
}
