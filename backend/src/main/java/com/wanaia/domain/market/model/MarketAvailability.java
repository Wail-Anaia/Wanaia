package com.wanaia.domain.market.model;

import com.wanaia.common.base.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "market_availabilities", uniqueConstraints = {
    @UniqueConstraint(name = "uq_product_market_year", columnNames = {"product_id", "market_id", "model_year_id"})
}, indexes = {
    @Index(name = "idx_avail_product", columnList = "product_id"),
    @Index(name = "idx_avail_market", columnList = "market_id")
})
public class MarketAvailability extends BaseEntity {

    @Column(name = "product_id", nullable = false)
    private Long productId; // Referenced by ID

    @Column(name = "market_id", nullable = false)
    private Long marketId; // Referenced by ID

    @Column(name = "model_year_id", nullable = false)
    private Long modelYearId; // Referenced by ID

    @Column(name = "local_trim_name", nullable = false, length = 150)
    private String localTrimName; // e.g. "Dynamic+", "Lounge", "Long Range AWD"

    @Column(name = "msrp_base_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal msrpBasePrice;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode; // "MAD", "EUR", "AED"

    @Column(name = "fiscal_horsepower_cv")
    private Integer fiscalHorsepowerCv; // e.g. 8 CV in Morocco/France

    @Column(name = "annual_vignette_tax", precision = 10, scale = 2)
    private BigDecimal annualVignetteTax;

    @Column(name = "warranty_years")
    private Integer warrantyYears = 3;

    @Column(name = "warranty_km")
    private Integer warrantyKm = 100000;

    @Column(name = "is_orderable", nullable = false)
    private Boolean isOrderable = true;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate = LocalDate.now();

    public MarketAvailability() {}

    public MarketAvailability(Long productId, Long marketId, Long modelYearId, String localTrimName,
                              BigDecimal msrpBasePrice, String currencyCode, Integer fiscalHorsepowerCv,
                              BigDecimal annualVignetteTax, Integer warrantyYears, Integer warrantyKm) {
        this.productId = productId;
        this.marketId = marketId;
        this.modelYearId = modelYearId;
        this.localTrimName = localTrimName;
        this.msrpBasePrice = msrpBasePrice;
        this.currencyCode = currencyCode;
        this.fiscalHorsepowerCv = fiscalHorsepowerCv;
        this.annualVignetteTax = annualVignetteTax;
        this.warrantyYears = warrantyYears;
        this.warrantyKm = warrantyKm;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public Long getModelYearId() {
        return modelYearId;
    }

    public void setModelYearId(Long modelYearId) {
        this.modelYearId = modelYearId;
    }

    public String getLocalTrimName() {
        return localTrimName;
    }

    public void setLocalTrimName(String localTrimName) {
        this.localTrimName = localTrimName;
    }

    public BigDecimal getMsrpBasePrice() {
        return msrpBasePrice;
    }

    public void setMsrpBasePrice(BigDecimal msrpBasePrice) {
        this.msrpBasePrice = msrpBasePrice;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Integer getFiscalHorsepowerCv() {
        return fiscalHorsepowerCv;
    }

    public void setFiscalHorsepowerCv(Integer fiscalHorsepowerCv) {
        this.fiscalHorsepowerCv = fiscalHorsepowerCv;
    }

    public BigDecimal getAnnualVignetteTax() {
        return annualVignetteTax;
    }

    public void setAnnualVignetteTax(BigDecimal annualVignetteTax) {
        this.annualVignetteTax = annualVignetteTax;
    }

    public Integer getWarrantyYears() {
        return warrantyYears;
    }

    public void setWarrantyYears(Integer warrantyYears) {
        this.warrantyYears = warrantyYears;
    }

    public Integer getWarrantyKm() {
        return warrantyKm;
    }

    public void setWarrantyKm(Integer warrantyKm) {
        this.warrantyKm = warrantyKm;
    }

    public Boolean getOrderable() {
        return isOrderable;
    }

    public void setOrderable(Boolean orderable) {
        isOrderable = orderable;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
