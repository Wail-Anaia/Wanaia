package com.wanaia.domain.mobility.repository;

import com.wanaia.domain.mobility.model.Generation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenerationRepository extends JpaRepository<Generation, Long> {
    Optional<Generation> findByModelIdAndSlug(Long modelId, String slug);
    List<Generation> findByModelId(Long modelId);
}
