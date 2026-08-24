package com.wanaia.domain.user.dto;

public record TokenRefreshRequest(
    String refreshToken,
    String clientType
) {}
