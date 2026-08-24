package com.wanaia.domain.mobility.service;

import com.wanaia.common.exception.ResourceNotFoundException;
import com.wanaia.domain.mobility.dto.*;
import com.wanaia.domain.mobility.model.*;
import com.wanaia.domain.mobility.port.inbound.MobilityProductQueryPort;
import com.wanaia.domain.mobility.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MobilityCatalogService implements MobilityProductQueryPort {

    private final MobilityCategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ModelRepository modelRepository;
    private final GenerationRepository generationRepository;
    private final MobilityProductRepository productRepository;
    private final PowertrainConfigurationRepository powertrainRepository;

    public MobilityCatalogService(MobilityCategoryRepository categoryRepository,
                                  BrandRepository brandRepository,
                                  ModelRepository modelRepository,
                                  GenerationRepository generationRepository,
                                  MobilityProductRepository productRepository,
                                  PowertrainConfigurationRepository powertrainRepository) {
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.modelRepository = modelRepository;
        this.generationRepository = generationRepository;
        this.productRepository = productRepository;
        this.powertrainRepository = powertrainRepository;
    }

    public List<MobilityCategoryDto> getCategories() {
        return categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc().stream()
            .map(c -> new MobilityCategoryDto(c.getId(), c.getCode(), c.getNameEn(), c.getNameFr(), c.getNameAr(), c.getIconUrl(), c.getDisplayOrder()))
            .collect(Collectors.toList());
    }

    public List<BrandDto> getBrands(Long categoryId) {
        List<Brand> brands = (categoryId != null)
            ? brandRepository.findByCategoryIdAndIsActiveTrueOrderByDisplayOrderAsc(categoryId)
            : brandRepository.findByIsActiveTrueOrderByDisplayOrderAsc();

        return brands.stream()
            .map(b -> new BrandDto(b.getId(), b.getCategoryId(), b.getName(), b.getSlug(), b.getLogoUrl(), b.getCountryOfOrigin(), b.getFoundedYear(), b.getWebsiteUrl()))
            .collect(Collectors.toList());
    }

    public Optional<BrandDto> getBrandBySlug(String slug) {
        return brandRepository.findBySlug(slug)
            .map(b -> new BrandDto(b.getId(), b.getCategoryId(), b.getName(), b.getSlug(), b.getLogoUrl(), b.getCountryOfOrigin(), b.getFoundedYear(), b.getWebsiteUrl()));
    }

    public List<ModelDto> getModelsByBrand(Long brandId) {
        return modelRepository.findByBrandIdAndIsActiveTrue(brandId).stream()
            .map(m -> new ModelDto(m.getId(), m.getBrandId(), m.getName(), m.getSlug(), m.getSegmentCode(), m.getDescription()))
            .collect(Collectors.toList());
    }

    public List<GenerationDto> getGenerationsByModel(Long modelId) {
        return generationRepository.findByModelId(modelId).stream()
            .map(g -> new GenerationDto(g.getId(), g.getModelId(), g.getName(), g.getSlug(), g.getInternalPlatformCode(), g.getStartYear(), g.getEndYear(), g.getHeroImageUrl(), g.getCurrent()))
            .collect(Collectors.toList());
    }

    @Override
    public Optional<MobilityProductDetailDto> getProductDetailById(Long id) {
        return productRepository.findById(id).map(this::mapProductToDetailDto);
    }

    @Override
    public Optional<MobilityProductDetailDto> getProductDetailByUuid(UUID uuid) {
        return productRepository.findByUuid(uuid).map(this::mapProductToDetailDto);
    }

    @Override
    public List<MobilityProductSummaryDto> getProductsByGenerationId(Long generationId) {
        return productRepository.findByGenerationIdAndIsActiveTrue(generationId).stream()
            .map(this::mapProductToSummaryDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<MobilityProductSummaryDto> getAllActiveProducts() {
        return productRepository.findAll().stream()
            .filter(MobilityProduct::getActive)
            .map(this::mapProductToSummaryDto)
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }

    private MobilityProductSummaryDto mapProductToSummaryDto(MobilityProduct p) {
        Generation gen = generationRepository.findById(p.getGenerationId()).orElse(null);
        Model model = (gen != null) ? modelRepository.findById(gen.getModelId()).orElse(null) : null;
        Brand brand = (model != null) ? brandRepository.findById(model.getBrandId()).orElse(null) : null;
        PowertrainConfiguration pt = powertrainRepository.findById(p.getPowertrainConfigId()).orElse(null);

        return new MobilityProductSummaryDto(
            p.getId(),
            p.getUuid(),
            brand != null ? brand.getName() : "",
            model != null ? model.getName() : "",
            gen != null ? gen.getName() : "",
            p.getVariantName(),
            p.getSlug(),
            p.getBodyStyle(),
            p.getSeatCount(),
            pt != null ? pt.getCombinedPowerHp() : 0,
            pt != null ? pt.getPropulsionType() : "ICE",
            pt != null ? pt.getPrimaryFuel() : "PETROL",
            pt != null ? pt.getWltpConsumptionMetric() : null,
            p.getSafetyRatingNcap()
        );
    }

    private MobilityProductDetailDto mapProductToDetailDto(MobilityProduct p) {
        Generation gen = generationRepository.findById(p.getGenerationId()).orElse(null);
        Model model = (gen != null) ? modelRepository.findById(gen.getModelId()).orElse(null) : null;
        Brand brand = (model != null) ? brandRepository.findById(model.getBrandId()).orElse(null) : null;
        PowertrainConfiguration pt = powertrainRepository.findById(p.getPowertrainConfigId()).orElse(null);

        PowertrainConfigurationDto ptDto = (pt != null) ? new PowertrainConfigurationDto(
            pt.getId(),
            pt.getCode(),
            pt.getPropulsionType(),
            pt.getPrimaryFuel(),
            pt.getDriveLayout(),
            pt.getCombinedPowerHp(),
            pt.getCombinedPowerKw(),
            pt.getCombinedTorqueNm(),
            pt.getAcceleration0100S(),
            pt.getTopSpeedKmh(),
            pt.getWltpConsumptionMetric(),
            pt.getWltpCo2Gkm(),
            pt.getEvRangeWltpKm()
        ) : null;

        return new MobilityProductDetailDto(
            p.getId(),
            p.getUuid(),
            p.getGenerationId(),
            brand != null ? brand.getName() : "",
            model != null ? model.getName() : "",
            gen != null ? gen.getName() : "",
            p.getVariantName(),
            p.getSlug(),
            p.getBodyStyle(),
            p.getSeatCount(),
            p.getCurbWeightKg(),
            p.getLengthMm(),
            p.getWidthMm(),
            p.getHeightMm(),
            p.getWheelbaseMm(),
            p.getBootCapacityLiters(),
            p.getSafetyRatingNcap(),
            ptDto
        );
    }
}
