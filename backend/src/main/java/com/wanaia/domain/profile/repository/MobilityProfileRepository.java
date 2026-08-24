package com.wanaia.domain.profile.repository;

import com.wanaia.domain.profile.model.MobilityProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MobilityProfileRepository extends JpaRepository<MobilityProfile, Long> {
    Optional<MobilityProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
