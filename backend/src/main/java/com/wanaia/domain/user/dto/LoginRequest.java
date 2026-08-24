package com.wanaia.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "Email is required")
    String email,

    @NotBlank(message = "Password is required")
    String password,

    String clientType, // "WEB" or "ANDROID"
    String deviceInfo
) {}
