package com.wanaia.domain.mobility.repository;

import com.wanaia.domain.mobility.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MobilityCategoryRepository extends JpaRepository<MobilityCategory, Long> {
    Optional<MobilityCategory> findByCode(String code);
    List<MobilityCategory> findByIsActiveTrueOrderByDisplayOrderAsc();
}
