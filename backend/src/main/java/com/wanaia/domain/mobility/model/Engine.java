package com.wanaia.domain.mobility.model;

import com.wanaia.common.base.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "engines")
public class Engine extends BaseEntity {

    @Column(name = "engine_code", length = 50)
    private String engineCode; // e.g. "A25A-FXS", "H4Dt", "EA888"

    @Column(name = "fuel_type", nullable = false, length = 30)
    private String fuelType; // "PETROL", "DIESEL", "LPG"

    @Column(name = "displacement_cc", nullable = false)
    private Integer displacementCc;

    @Column(name = "cylinders", nullable = false)
    private Integer cylinders;

    @Column(name = "valves_per_cylinder")
    private Integer valvesPerCylinder = 4;

    @Column(name = "aspiration", length = 30)
    private String aspiration = "TURBOCHARGED"; // "NATURALLY_ASPIRATED", "TURBOCHARGED", "SUPERCHARGED"

    @Column(name = "power_hp", nullable = false)
    private Integer powerHp;

    @Column(name = "torque_nm", nullable = false)
    private Integer torqueNm;

    public Engine() {}

    public Engine(String engineCode, String fuelType, Integer displacementCc, Integer cylinders, Integer powerHp, Integer torqueNm) {
        this.engineCode = engineCode;
        this.fuelType = fuelType;
        this.displacementCc = displacementCc;
        this.cylinders = cylinders;
        this.powerHp = powerHp;
        this.torqueNm = torqueNm;
    }

    public String getEngineCode() {
        return engineCode;
    }

    public void setEngineCode(String engineCode) {
        this.engineCode = engineCode;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public Integer getDisplacementCc() {
        return displacementCc;
    }

    public void setDisplacementCc(Integer displacementCc) {
        this.displacementCc = displacementCc;
    }

    public Integer getCylinders() {
        return cylinders;
    }

    public void setCylinders(Integer cylinders) {
        this.cylinders = cylinders;
    }

    public Integer getValvesPerCylinder() {
        return valvesPerCylinder;
    }

    public void setValvesPerCylinder(Integer valvesPerCylinder) {
        this.valvesPerCylinder = valvesPerCylinder;
    }

    public String getAspiration() {
        return aspiration;
    }

    public void setAspiration(String aspiration) {
        this.aspiration = aspiration;
    }

    public Integer getPowerHp() {
        return powerHp;
    }

    public void setPowerHp(Integer powerHp) {
        this.powerHp = powerHp;
    }

    public Integer getTorqueNm() {
        return torqueNm;
    }

    public void setTorqueNm(Integer torqueNm) {
        this.torqueNm = torqueNm;
    }
}
