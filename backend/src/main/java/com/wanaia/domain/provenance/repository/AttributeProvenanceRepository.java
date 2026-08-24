package com.wanaia.domain.provenance.repository;

import com.wanaia.domain.provenance.model.AttributeProvenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeProvenanceRepository extends JpaRepository<AttributeProvenance, Long> {
    List<AttributeProvenance> findByEntityTypeAndEntityId(String entityType, Long entityId);
    List<AttributeProvenance> findByEntityTypeAndEntityIdAndMarketCode(String entityType, Long entityId, String marketCode);
}
