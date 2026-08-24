package com.wanaia.domain.mobility.dto;

public record GenerationDto(
    Long id,
    Long modelId,
    String name,
    String slug,
    String internalPlatformCode,
    Integer startYear,
    Integer endYear,
    String heroImageUrl,
    Boolean isCurrent
) {}
