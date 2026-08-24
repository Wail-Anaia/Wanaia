package com.wanaia.domain.mobility.dto;

public record ModelDto(
    Long id,
    Long brandId,
    String name,
    String slug,
    String segmentCode,
    String description
) {}
