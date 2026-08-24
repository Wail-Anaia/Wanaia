package com.wanaia.domain.market.model;

import com.wanaia.common.base.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "countries")
public class Country extends BaseEntity {

    @Column(name = "iso_code_2", nullable = false, unique = true, length = 2)
    private String isoCode2; // "MA", "FR", "AE"

    @Column(name = "iso_code_3", nullable = false, unique = true, length = 3)
    private String isoCode3; // "MAR", "FRA", "ARE"

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "currency_symbol", length = 10)
    private String currencySymbol = "DH";

    @Column(name = "dialing_code", length = 10)
    private String dialingCode = "+212";

    public Country() {}

    public Country(String isoCode2, String isoCode3, String name, String currencySymbol, String dialingCode) {
        this.isoCode2 = isoCode2;
        this.isoCode3 = isoCode3;
        this.name = name;
        this.currencySymbol = currencySymbol;
        this.dialingCode = dialingCode;
    }

    public String getIsoCode2() {
        return isoCode2;
    }

    public void setIsoCode2(String isoCode2) {
        this.isoCode2 = isoCode2;
    }

    public String getIsoCode3() {
        return isoCode3;
    }

    public void setIsoCode3(String isoCode3) {
        this.isoCode3 = isoCode3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getDialingCode() {
        return dialingCode;
    }

    public void setDialingCode(String dialingCode) {
        this.dialingCode = dialingCode;
    }
}
