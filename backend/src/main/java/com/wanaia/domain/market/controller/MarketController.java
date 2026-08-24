package com.wanaia.domain.market.controller;

import com.wanaia.common.api.ApiResponse;
import com.wanaia.common.exception.ResourceNotFoundException;
import com.wanaia.domain.market.dto.MarketAvailabilityDto;
import com.wanaia.domain.market.dto.MarketDto;
import com.wanaia.domain.market.port.inbound.MarketPricingPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/markets")
@Tag(name = "Markets & Localization", description = "Endpoints for sovereign markets, localized pricing, taxes, and availability.")
public class MarketController {

    private final MarketPricingPort marketPricingPort;

    public MarketController(MarketPricingPort marketPricingPort) {
        this.marketPricingPort = marketPricingPort;
    }

    @GetMapping
    @Operation(summary = "List all active sovereign markets (Morocco, France, UAE, etc.)")
    public ResponseEntity<ApiResponse<List<MarketDto>>> getActiveMarkets() {
        return ResponseEntity.ok(ApiResponse.success(marketPricingPort.getActiveMarkets()));
    }

    @GetMapping("/{code}")
    @Operation(summary = "Get market details by ISO code")
    public ResponseEntity<ApiResponse<MarketDto>> getMarketByCode(@PathVariable String code) {
        MarketDto market = marketPricingPort.getMarketByCode(code)
            .orElseThrow(() -> new ResourceNotFoundException("Market", "code", code));
        return ResponseEntity.ok(ApiResponse.success(market));
    }

    @GetMapping("/products/{productId}")
    @Operation(summary = "Get all market pricing and availability records for a mobility product")
    public ResponseEntity<ApiResponse<List<MarketAvailabilityDto>>> getAvailabilitiesForProduct(
        @PathVariable Long productId,
        @RequestParam(required = false) String marketCode
    ) {
        List<MarketAvailabilityDto> list = (marketCode != null && !marketCode.trim().isEmpty())
            ? marketPricingPort.getAvailabilitiesForProductInMarket(productId, marketCode)
            : marketPricingPort.getAvailabilitiesForProduct(productId);
        return ResponseEntity.ok(ApiResponse.success(list));
    }
}
