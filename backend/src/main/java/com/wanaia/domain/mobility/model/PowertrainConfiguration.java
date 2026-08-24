package com.wanaia.domain.mobility.model;

import com.wanaia.common.base.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "powertrain_configurations")
public class PowertrainConfiguration extends BaseEntity {

    @Column(name = "code", nullable = false, unique = true, length = 80)
    private String code; // e.g. "TOYOTA_THS_2.5_AWD", "RENAULT_E_TECH_145", "TESLA_DUAL_MOTOR_LR"

    @Column(name = "propulsion_type", nullable = false, length = 30)
    private String propulsionType; // "ICE", "MHEV", "HEV", "PHEV", "BEV", "FCEV"

    @Column(name = "primary_fuel", nullable = false, length = 30)
    private String primaryFuel; // "PETROL", "DIESEL", "ELECTRICITY", "HYDROGEN"

    @Column(name = "drive_layout", nullable = false, length = 20)
    private String driveLayout = "FWD"; // "FWD", "RWD", "AWD", "4WD", "CHAIN_DRIVE"

    // Component References (Referenced by ID across Aggregate Boundaries)
    @Column(name = "engine_id")
    private Long engineId;

    @Column(name = "primary_motor_id")
    private Long primaryMotorId;

    @Column(name = "secondary_motor_id")
    private Long secondaryMotorId;

    @Column(name = "battery_id")
    private Long batteryId;

    @Column(name = "transmission_id")
    private Long transmissionId;

    // Combined Performance Output
    @Column(name = "combined_power_hp", nullable = false)
    private Integer combinedPowerHp;

    @Column(name = "combined_power_kw", nullable = false)
    private Integer combinedPowerKw;

    @Column(name = "combined_torque_nm", nullable = false)
    private Integer combinedTorqueNm;

    @Column(name = "acceleration_0_100_s", precision = 4, scale = 2)
    private BigDecimal acceleration0100S;

    @Column(name = "top_speed_kmh")
    private Integer topSpeedKmh;

    // Efficiency Metrics
    @Column(name = "wltp_consumption_metric", precision = 4, scale = 1)
    private BigDecimal wltpConsumptionMetric; // L/100km or kWh/100km

    @Column(name = "wltp_co2_gkm")
    private Integer wltpCo2Gkm;

    @Column(name = "ev_range_wltp_km")
    private Integer evRangeWltpKm;

    public PowertrainConfiguration() {}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPropulsionType() {
        return propulsionType;
    }

    public void setPropulsionType(String propulsionType) {
        this.propulsionType = propulsionType;
    }

    public String getPrimaryFuel() {
        return primaryFuel;
    }

    public void setPrimaryFuel(String primaryFuel) {
        this.primaryFuel = primaryFuel;
    }

    public String getDriveLayout() {
        return driveLayout;
    }

    public void setDriveLayout(String driveLayout) {
        this.driveLayout = driveLayout;
    }

    public Long getEngineId() {
        return engineId;
    }

    public void setEngineId(Long engineId) {
        this.engineId = engineId;
    }

    public Long getPrimaryMotorId() {
        return primaryMotorId;
    }

    public void setPrimaryMotorId(Long primaryMotorId) {
        this.primaryMotorId = primaryMotorId;
    }

    public Long getSecondaryMotorId() {
        return secondaryMotorId;
    }

    public void setSecondaryMotorId(Long secondaryMotorId) {
        this.secondaryMotorId = secondaryMotorId;
    }

    public Long getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(Long batteryId) {
        this.batteryId = batteryId;
    }

    public Long getTransmissionId() {
        return transmissionId;
    }

    public void setTransmissionId(Long transmissionId) {
        this.transmissionId = transmissionId;
    }

    public Integer getCombinedPowerHp() {
        return combinedPowerHp;
    }

    public void setCombinedPowerHp(Integer combinedPowerHp) {
        this.combinedPowerHp = combinedPowerHp;
    }

    public Integer getCombinedPowerKw() {
        return combinedPowerKw;
    }

    public void setCombinedPowerKw(Integer combinedPowerKw) {
        this.combinedPowerKw = combinedPowerKw;
    }

    public Integer getCombinedTorqueNm() {
        return combinedTorqueNm;
    }

    public void setCombinedTorqueNm(Integer combinedTorqueNm) {
        this.combinedTorqueNm = combinedTorqueNm;
    }

    public BigDecimal getAcceleration0100S() {
        return acceleration0100S;
    }

    public void setAcceleration0100S(BigDecimal acceleration0100S) {
        this.acceleration0100S = acceleration0100S;
    }

    public Integer getTopSpeedKmh() {
        return topSpeedKmh;
    }

    public void setTopSpeedKmh(Integer topSpeedKmh) {
        this.topSpeedKmh = topSpeedKmh;
    }

    public BigDecimal getWltpConsumptionMetric() {
        return wltpConsumptionMetric;
    }

    public void setWltpConsumptionMetric(BigDecimal wltpConsumptionMetric) {
        this.wltpConsumptionMetric = wltpConsumptionMetric;
    }

    public Integer getWltpCo2Gkm() {
        return wltpCo2Gkm;
    }

    public void setWltpCo2Gkm(Integer wltpCo2Gkm) {
        this.wltpCo2Gkm = wltpCo2Gkm;
    }

    public Integer getEvRangeWltpKm() {
        return evRangeWltpKm;
    }

    public void setEvRangeWltpKm(Integer evRangeWltpKm) {
        this.evRangeWltpKm = evRangeWltpKm;
    }
}
