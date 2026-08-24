package com.wanaia.domain.mobility.model;

import com.wanaia.common.base.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "mobility_categories")
public class MobilityCategory extends BaseEntity {

    @Column(name = "code", nullable = false, unique = true, length = 30)
    private String code; // 'CAR', 'MOTORCYCLE', 'SCOOTER', 'COMMERCIAL_VAN', 'TRUCK', 'E_BIKE'

    @Column(name = "name_en", nullable = false, length = 50)
    private String nameEn;

    @Column(name = "name_fr", nullable = false, length = 50)
    private String nameFr;

    @Column(name = "name_ar", nullable = false, length = 50)
    private String nameAr;

    @Column(name = "icon_url", length = 500)
    private String iconUrl;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public MobilityCategory() {}

    public MobilityCategory(String code, String nameEn, String nameFr, String nameAr, String iconUrl, Integer displayOrder) {
        this.code = code;
        this.nameEn = nameEn;
        this.nameFr = nameFr;
        this.nameAr = nameAr;
        this.iconUrl = iconUrl;
        this.displayOrder = displayOrder;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameFr() {
        return nameFr;
    }

    public void setNameFr(String nameFr) {
        this.nameFr = nameFr;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
