package com.wanaia.domain.mobility.repository;

import com.wanaia.domain.mobility.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PowertrainConfigurationRepository extends JpaRepository<PowertrainConfiguration, Long> {
    Optional<PowertrainConfiguration> findByCode(String code);
}
