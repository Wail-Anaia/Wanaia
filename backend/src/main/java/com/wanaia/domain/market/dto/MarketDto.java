package com.wanaia.domain.market.dto;

public record MarketDto(
    Long id,
    String code,
    String name,
    String currencyCode,
    String defaultLocale
) {}
