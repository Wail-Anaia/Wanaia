package com.wanaia.domain.market.model;

import com.wanaia.common.base.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "markets")
public class Market extends BaseEntity {

    @Column(name = "code", nullable = false, unique = true, length = 3)
    private String code; // e.g. "MAR", "FRA", "ARE"

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode; // "MAD", "EUR", "AED"

    @Column(name = "default_locale", nullable = false, length = 10)
    private String defaultLocale = "fr-MA";

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public Market() {}

    public Market(String code, String name, String currencyCode, String defaultLocale) {
        this.code = code;
        this.name = name;
        this.currencyCode = currencyCode;
        this.defaultLocale = defaultLocale;
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

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getDefaultLocale() {
        return defaultLocale;
    }

    public void setDefaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
