package com.wanaia.domain.mobility.dto;

public record MobilityCategoryDto(
    Long id,
    String code,
    String nameEn,
    String nameFr,
    String nameAr,
    String iconUrl,
    Integer displayOrder
) {}
