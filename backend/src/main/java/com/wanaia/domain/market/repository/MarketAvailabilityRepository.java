package com.wanaia.domain.market.repository;

import com.wanaia.domain.market.model.MarketAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarketAvailabilityRepository extends JpaRepository<MarketAvailability, Long> {
    List<MarketAvailability> findByProductId(Long productId);
    List<MarketAvailability> findByProductIdAndMarketId(Long productId, Long marketId);
    Optional<MarketAvailability> findByProductIdAndMarketIdAndModelYearId(Long productId, Long marketId, Long modelYearId);
}
