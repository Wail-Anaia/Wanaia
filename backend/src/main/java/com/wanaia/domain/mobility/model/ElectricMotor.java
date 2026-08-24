package com.wanaia.domain.mobility.model;

import com.wanaia.common.base.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "electric_motors")
public class ElectricMotor extends BaseEntity {

    @Column(name = "motor_code", length = 50)
    private String motorCode;

    @Column(name = "motor_type", length = 40)
    private String motorType = "PERMANENT_MAGNET_SYNCHRONOUS"; // "PERMANENT_MAGNET_SYNCHRONOUS", "INDUCTION", "CURRENT_EXCITED"

    @Column(name = "position", length = 30)
    private String position = "FRONT_AXLE"; // "FRONT_AXLE", "REAR_AXLE", "HUB"

    @Column(name = "power_kw", nullable = false)
    private Integer powerKw;

    @Column(name = "torque_nm", nullable = false)
    private Integer torqueNm;

    public ElectricMotor() {}

    public ElectricMotor(String motorCode, String motorType, String position, Integer powerKw, Integer torqueNm) {
        this.motorCode = motorCode;
        this.motorType = motorType;
        this.position = position;
        this.powerKw = powerKw;
        this.torqueNm = torqueNm;
    }

    public String getMotorCode() {
        return motorCode;
    }

    public void setMotorCode(String motorCode) {
        this.motorCode = motorCode;
    }

    public String getMotorType() {
        return motorType;
    }

    public void setMotorType(String motorType) {
        this.motorType = motorType;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getPowerKw() {
        return powerKw;
    }

    public void setPowerKw(Integer powerKw) {
        this.powerKw = powerKw;
    }

    public Integer getTorqueNm() {
        return torqueNm;
    }

    public void setTorqueNm(Integer torqueNm) {
        this.torqueNm = torqueNm;
    }
}
