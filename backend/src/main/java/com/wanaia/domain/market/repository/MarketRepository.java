package com.wanaia.domain.market.repository;

import com.wanaia.domain.market.model.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarketRepository extends JpaRepository<Market, Long> {
    Optional<Market> findByCode(String code);
    List<Market> findByIsActiveTrue();
}
