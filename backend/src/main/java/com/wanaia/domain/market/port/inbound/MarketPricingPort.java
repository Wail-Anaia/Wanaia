package com.wanaia.domain.market.port.inbound;

import com.wanaia.domain.market.dto.MarketAvailabilityDto;
import com.wanaia.domain.market.dto.MarketDto;

import java.util.List;
import java.util.Optional;

public interface MarketPricingPort {
    List<MarketDto> getActiveMarkets();
    Optional<MarketDto> getMarketByCode(String code);
    List<MarketAvailabilityDto> getAvailabilitiesForProduct(Long productId);
    List<MarketAvailabilityDto> getAvailabilitiesForProductInMarket(Long productId, String marketCode);
    Optional<MarketAvailabilityDto> getAvailability(Long productId, String marketCode, Integer modelYear);
}
