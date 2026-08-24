package com.wanaia.domain.mobility.model;

import com.wanaia.common.base.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "transmissions")
public class Transmission extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "transmission_type", nullable = false, length = 40)
    private String transmissionType; // "MANUAL", "TORQUE_CONVERTER_AUTO", "DUAL_CLUTCH", "CVT", "SINGLE_SPEED_REDUCER"

    @Column(name = "gear_count")
    private Integer gearCount = 1;

    public Transmission() {}

    public Transmission(String name, String transmissionType, Integer gearCount) {
        this.name = name;
        this.transmissionType = transmissionType;
        this.gearCount = gearCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(String transmissionType) {
        this.transmissionType = transmissionType;
    }

    public Integer getGearCount() {
        return gearCount;
    }

    public void setGearCount(Integer gearCount) {
        this.gearCount = gearCount;
    }
}
