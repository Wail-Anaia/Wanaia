package com.wanaia.domain.user.dto;

import java.util.UUID;

public record AuthResponse(
    String accessToken,
    String refreshToken, // Returned in body for Android; in HttpOnly cookie for Web
    long expiresInMs,
    UserSummary user
) {
    public record UserSummary(
        Long id,
        UUID uuid,
        String email,
        String displayName,
        String role,
        String locale,
        String countryCode
    ) {}
}
