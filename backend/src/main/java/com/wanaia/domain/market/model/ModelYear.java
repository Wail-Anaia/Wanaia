package com.wanaia.domain.market.model;

import com.wanaia.common.base.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "model_years")
public class ModelYear extends BaseEntity {

    @Column(name = "year", nullable = false, unique = true)
    private Integer year;

    public ModelYear() {}

    public ModelYear(Integer year) {
        this.year = year;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
