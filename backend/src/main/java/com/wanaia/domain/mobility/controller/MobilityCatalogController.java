package com.wanaia.domain.mobility.controller;

import com.wanaia.common.api.ApiResponse;
import com.wanaia.common.exception.ResourceNotFoundException;
import com.wanaia.domain.mobility.dto.*;
import com.wanaia.domain.mobility.service.MobilityCatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/mobility")
@Tag(name = "Mobility Catalog", description = "Endpoints for exploring mobility categories, brands, models, generations, and product specifications.")
public class MobilityCatalogController {

    private final MobilityCatalogService catalogService;

    public MobilityCatalogController(MobilityCatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/categories")
    @Operation(summary = "List all active mobility categories (Cars, Motorcycles, Vans, etc.)")
    public ResponseEntity<ApiResponse<List<MobilityCategoryDto>>> getCategories() {
        return ResponseEntity.ok(ApiResponse.success(catalogService.getCategories()));
    }

    @GetMapping("/brands")
    @Operation(summary = "List brands, optionally filtered by category")
    public ResponseEntity<ApiResponse<List<BrandDto>>> getBrands(@RequestParam(required = false) Long categoryId) {
        return ResponseEntity.ok(ApiResponse.success(catalogService.getBrands(categoryId)));
    }

    @GetMapping("/brands/{slug}")
    @Operation(summary = "Get brand details by slug")
    public ResponseEntity<ApiResponse<BrandDto>> getBrandBySlug(@PathVariable String slug) {
        BrandDto brand = catalogService.getBrandBySlug(slug)
            .orElseThrow(() -> new ResourceNotFoundException("Brand", "slug", slug));
        return ResponseEntity.ok(ApiResponse.success(brand));
    }

    @GetMapping("/models/by-brand/{brandId}")
    @Operation(summary = "Get models for a specific brand")
    public ResponseEntity<ApiResponse<List<ModelDto>>> getModelsByBrand(@PathVariable Long brandId) {
        return ResponseEntity.ok(ApiResponse.success(catalogService.getModelsByBrand(brandId)));
    }

    @GetMapping("/generations/by-model/{modelId}")
    @Operation(summary = "Get generations for a specific model")
    public ResponseEntity<ApiResponse<List<GenerationDto>>> getGenerationsByModel(@PathVariable Long modelId) {
        return ResponseEntity.ok(ApiResponse.success(catalogService.getGenerationsByModel(modelId)));
    }

    @GetMapping("/products/{id}")
    @Operation(summary = "Get mobility product variant details by ID")
    public ResponseEntity<ApiResponse<MobilityProductDetailDto>> getProductById(@PathVariable Long id) {
        MobilityProductDetailDto product = catalogService.getProductDetailById(id)
            .orElseThrow(() -> new ResourceNotFoundException("MobilityProduct", "id", id));
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    @GetMapping("/products/uuid/{uuid}")
    @Operation(summary = "Get mobility product variant details by UUID")
    public ResponseEntity<ApiResponse<MobilityProductDetailDto>> getProductByUuid(@PathVariable UUID uuid) {
        MobilityProductDetailDto product = catalogService.getProductDetailByUuid(uuid)
            .orElseThrow(() -> new ResourceNotFoundException("MobilityProduct", "uuid", uuid));
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    @GetMapping("/products/by-generation/{generationId}")
    @Operation(summary = "Get product variants for a generation")
    public ResponseEntity<ApiResponse<List<MobilityProductSummaryDto>>> getProductsByGeneration(@PathVariable Long generationId) {
        return ResponseEntity.ok(ApiResponse.success(catalogService.getProductsByGenerationId(generationId)));
    }
}
