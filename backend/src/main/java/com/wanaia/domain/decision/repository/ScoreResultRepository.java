package com.wanaia.domain.decision.repository;

import com.wanaia.domain.decision.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreResultRepository extends JpaRepository<ScoreResult, Long> {
    Optional<ScoreResult> findFirstByEntityTypeAndEntityIdAndScoreTypeOrderByCalculatedAtDesc(
        String entityType, Long entityId, ScoreType scoreType
    );
    List<ScoreResult> findByEntityTypeAndEntityId(String entityType, Long entityId);
}
