package com.wanaia.domain.decision.repository;

import com.wanaia.domain.decision.model.ScoreExplanationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreExplanationItemRepository extends JpaRepository<ScoreExplanationItem, Long> {
    List<ScoreExplanationItem> findByScoreResultId(Long scoreResultId);
}
