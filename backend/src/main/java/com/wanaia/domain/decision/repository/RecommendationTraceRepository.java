package com.wanaia.domain.decision.repository;

import com.wanaia.domain.decision.model.RecommendationTrace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationTraceRepository extends JpaRepository<RecommendationTrace, Long> {
    List<RecommendationTrace> findByUserIdOrderByGeneratedAtDesc(Long userId);
}
