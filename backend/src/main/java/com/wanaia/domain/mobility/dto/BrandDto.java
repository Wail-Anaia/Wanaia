package com.wanaia.domain.mobility.dto;

public record BrandDto(
    Long id,
    Long categoryId,
    String name,
    String slug,
    String logoUrl,
    String countryOfOrigin,
    Integer foundedYear,
    String websiteUrl
) {}
