package com.wanaia.domain.mobility.model;

import com.wanaia.common.base.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "batteries")
public class Battery extends BaseEntity {

    @Column(name = "battery_code", length = 50)
    private String batteryCode;

    @Column(name = "chemistry", length = 30)
    private String chemistry = "NMC"; // "NMC", "LFP", "NCA", "SOLID_STATE"

    @Column(name = "gross_capacity_kwh", precision = 6, scale = 2)
    private BigDecimal grossCapacityKwh;

    @Column(name = "usable_capacity_kwh", precision = 6, scale = 2)
    private BigDecimal usableCapacityKwh;

    @Column(name = "nominal_voltage")
    private Integer nominalVoltage = 400; // e.g. 400V, 800V

    @Column(name = "max_dc_charge_kw")
    private Integer maxDcChargeKw;

    @Column(name = "max_ac_charge_kw")
    private Integer maxAcChargeKw = 11;

    @Column(name = "charge_time_10_80_min")
    private Integer chargeTime1080Min;

    @Column(name = "thermal_management", length = 30)
    private String thermalManagement = "LIQUID_COOLED";

    public Battery() {}

    public Battery(String batteryCode, String chemistry, BigDecimal usableCapacityKwh, Integer maxDcChargeKw, Integer chargeTime1080Min) {
        this.batteryCode = batteryCode;
        this.chemistry = chemistry;
        this.usableCapacityKwh = usableCapacityKwh;
        this.maxDcChargeKw = maxDcChargeKw;
        this.chargeTime1080Min = chargeTime1080Min;
    }

    public String getBatteryCode() {
        return batteryCode;
    }

    public void setBatteryCode(String batteryCode) {
        this.batteryCode = batteryCode;
    }

    public String getChemistry() {
        return chemistry;
    }

    public void setChemistry(String chemistry) {
        this.chemistry = chemistry;
    }

    public BigDecimal getGrossCapacityKwh() {
        return grossCapacityKwh;
    }

    public void setGrossCapacityKwh(BigDecimal grossCapacityKwh) {
        this.grossCapacityKwh = grossCapacityKwh;
    }

    public BigDecimal getUsableCapacityKwh() {
        return usableCapacityKwh;
    }

    public void setUsableCapacityKwh(BigDecimal usableCapacityKwh) {
        this.usableCapacityKwh = usableCapacityKwh;
    }

    public Integer getNominalVoltage() {
        return nominalVoltage;
    }

    public void setNominalVoltage(Integer nominalVoltage) {
        this.nominalVoltage = nominalVoltage;
    }

    public Integer getMaxDcChargeKw() {
        return maxDcChargeKw;
    }

    public void setMaxDcChargeKw(Integer maxDcChargeKw) {
        this.maxDcChargeKw = maxDcChargeKw;
    }

    public Integer getMaxAcChargeKw() {
        return maxAcChargeKw;
    }

    public void setMaxAcChargeKw(Integer maxAcChargeKw) {
        this.maxAcChargeKw = maxAcChargeKw;
    }

    public Integer getChargeTime1080Min() {
        return chargeTime1080Min;
    }

    public void setChargeTime1080Min(Integer chargeTime1080Min) {
        this.chargeTime1080Min = chargeTime1080Min;
    }

    public String getThermalManagement() {
        return thermalManagement;
    }

    public void setThermalManagement(String thermalManagement) {
        this.thermalManagement = thermalManagement;
    }
}
