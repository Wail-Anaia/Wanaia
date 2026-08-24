package com.wanaia.domain.mobility.repository;

import com.wanaia.domain.mobility.model.Battery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BatteryRepository extends JpaRepository<Battery, Long> {
    Optional<Battery> findByBatteryCode(String batteryCode);
}
