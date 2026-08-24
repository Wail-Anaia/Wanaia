package com.wanaia.domain.market.service;

import com.wanaia.domain.market.dto.MarketAvailabilityDto;
import com.wanaia.domain.market.dto.MarketDto;
import com.wanaia.domain.market.model.Market;
import com.wanaia.domain.market.model.MarketAvailability;
import com.wanaia.domain.market.model.ModelYear;
import com.wanaia.domain.market.port.inbound.MarketPricingPort;
import com.wanaia.domain.market.repository.MarketAvailabilityRepository;
import com.wanaia.domain.market.repository.MarketRepository;
import com.wanaia.domain.market.repository.ModelYearRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MarketLocalizationService implements MarketPricingPort {

    private final MarketRepository marketRepository;
    private final ModelYearRepository modelYearRepository;
    private final MarketAvailabilityRepository availabilityRepository;

    public MarketLocalizationService(MarketRepository marketRepository,
                                     ModelYearRepository modelYearRepository,
                                     MarketAvailabilityRepository availabilityRepository) {
        this.marketRepository = marketRepository;
        this.modelYearRepository = modelYearRepository;
        this.availabilityRepository = availabilityRepository;
    }

    @Override
    public List<MarketDto> getActiveMarkets() {
        return marketRepository.findByIsActiveTrue().stream()
            .map(m -> new MarketDto(m.getId(), m.getCode(), m.getName(), m.getCurrencyCode(), m.getDefaultLocale()))
            .collect(Collectors.toList());
    }

    @Override
    public Optional<MarketDto> getMarketByCode(String code) {
        return marketRepository.findByCode(code)
            .map(m -> new MarketDto(m.getId(), m.getCode(), m.getName(), m.getCurrencyCode(), m.getDefaultLocale()));
    }

    @Override
    public List<MarketAvailabilityDto> getAvailabilitiesForProduct(Long productId) {
        return availabilityRepository.findByProductId(productId).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<MarketAvailabilityDto> getAvailabilitiesForProductInMarket(Long productId, String marketCode) {
        Optional<Market> marketOpt = marketRepository.findByCode(marketCode);
        if (marketOpt.isEmpty()) return List.of();

        return availabilityRepository.findByProductIdAndMarketId(productId, marketOpt.get().getId()).stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<MarketAvailabilityDto> getAvailability(Long productId, String marketCode, Integer modelYear) {
        Optional<Market> marketOpt = marketRepository.findByCode(marketCode);
        Optional<ModelYear> myOpt = modelYearRepository.findByYear(modelYear);

        if (marketOpt.isEmpty() || myOpt.isEmpty()) return Optional.empty();

        return availabilityRepository.findByProductIdAndMarketIdAndModelYearId(productId, marketOpt.get().getId(), myOpt.get().getId())
            .map(this::mapToDto);
    }

    private MarketAvailabilityDto mapToDto(MarketAvailability a) {
        Market market = marketRepository.findById(a.getMarketId()).orElse(null);
        ModelYear my = modelYearRepository.findById(a.getModelYearId()).orElse(null);

        return new MarketAvailabilityDto(
            a.getId(),
            a.getProductId(),
            a.getMarketId(),
            market != null ? market.getCode() : "",
            a.getModelYearId(),
            my != null ? my.getYear() : 0,
            a.getLocalTrimName(),
            a.getMsrpBasePrice(),
            a.getCurrencyCode(),
            a.getFiscalHorsepowerCv(),
            a.getAnnualVignetteTax(),
            a.getWarrantyYears(),
            a.getWarrantyKm(),
            a.getOrderable(),
            a.getEffectiveDate()
        );
    }
}
