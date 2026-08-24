package com.wanaia.domain.mobility.repository;

import com.wanaia.domain.mobility.model.MobilityProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MobilityProductRepository extends JpaRepository<MobilityProduct, Long> {
    Optional<MobilityProduct> findByUuid(UUID uuid);
    Optional<MobilityProduct> findByGenerationIdAndSlug(Long generationId, String slug);
    List<MobilityProduct> findByGenerationIdAndIsActiveTrue(Long generationId);
}
