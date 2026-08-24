package com.wanaia.domain.provenance.dto;

public record DataSourceDto(
    Long id,
    String code,
    String name,
    String sourceType,
    Integer trustTier,
    String websiteUrl
) {}
