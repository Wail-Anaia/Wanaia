package com.wanaia.domain.decision.repository;

import com.wanaia.domain.decision.model.ScoreInputSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScoreInputSnapshotRepository extends JpaRepository<ScoreInputSnapshot, Long> {
    Optional<ScoreInputSnapshot> findBySnapshotHash(String snapshotHash);
}
