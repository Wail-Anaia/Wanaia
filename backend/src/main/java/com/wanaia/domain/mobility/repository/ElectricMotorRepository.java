package com.wanaia.domain.mobility.repository;

import com.wanaia.domain.mobility.model.Battery;
import com.wanaia.domain.mobility.model.ElectricMotor;
import com.wanaia.domain.mobility.model.Transmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ElectricMotorRepository extends JpaRepository<ElectricMotor, Long> {
    Optional<ElectricMotor> findByMotorCode(String motorCode);
}
