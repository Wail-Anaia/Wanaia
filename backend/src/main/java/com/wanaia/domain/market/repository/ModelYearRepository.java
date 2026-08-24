package com.wanaia.domain.market.repository;

import com.wanaia.domain.market.model.MarketAvailability;
import com.wanaia.domain.market.model.ModelYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModelYearRepository extends JpaRepository<ModelYear, Long> {
    Optional<ModelYear> findByYear(Integer year);
}
