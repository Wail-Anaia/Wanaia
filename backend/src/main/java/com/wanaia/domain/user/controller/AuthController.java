package com.wanaia.domain.user.controller;

import com.wanaia.common.api.ApiResponse;
import com.wanaia.domain.user.dto.AuthResponse;
import com.wanaia.domain.user.dto.LoginRequest;
import com.wanaia.domain.user.dto.RegisterRequest;
import com.wanaia.domain.user.dto.TokenRefreshRequest;
import com.wanaia.domain.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication & Security", description = "Endpoints for user registration, authentication, token rotation, and logout.")
public class AuthController {

    private static final String REFRESH_COOKIE_NAME = "wanaia_refresh_token";
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user account")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
        @Valid @RequestBody RegisterRequest request,
        HttpServletResponse response
    ) {
        AuthResponse authResponse = authService.register(request);
        setRefreshCookie(response, authResponse.refreshToken());
        return ResponseEntity.ok(ApiResponse.success(authResponse));
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user credentials and receive JWT access token")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
        @Valid @RequestBody LoginRequest request,
        HttpServletRequest httpRequest,
        HttpServletResponse response
    ) {
        String clientIp = httpRequest.getRemoteAddr();
        AuthResponse authResponse = authService.login(request, clientIp);

        // Web clients receive HttpOnly cookie; Android receives body
        if (request.clientType() == null || !request.clientType().equalsIgnoreCase("ANDROID")) {
            setRefreshCookie(response, authResponse.refreshToken());
        }

        return ResponseEntity.ok(ApiResponse.success(authResponse));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh expired JWT access token using HttpOnly cookie (Web) or request body (Android)")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
        @RequestBody(required = false) TokenRefreshRequest body,
        @CookieValue(name = REFRESH_COOKIE_NAME, required = false) String cookieRefreshToken,
        HttpServletResponse response
    ) {
        String tokenToUse = (body != null && body.refreshToken() != null && !body.refreshToken().trim().isEmpty())
            ? body.refreshToken()
            : cookieRefreshToken;

        String clientType = (body != null && body.clientType() != null) ? body.clientType() : "WEB";
        AuthResponse authResponse = authService.refreshAccessToken(tokenToUse, clientType);

        if (!clientType.equalsIgnoreCase("ANDROID")) {
            setRefreshCookie(response, authResponse.refreshToken());
        }

        return ResponseEntity.ok(ApiResponse.success(authResponse));
    }

    @PostMapping("/logout")
    @Operation(summary = "Log out user and revoke active refresh token")
    public ResponseEntity<ApiResponse<Void>> logout(
        @RequestBody(required = false) TokenRefreshRequest body,
        @CookieValue(name = REFRESH_COOKIE_NAME, required = false) String cookieRefreshToken,
        HttpServletResponse response
    ) {
        String tokenToUse = (body != null && body.refreshToken() != null) ? body.refreshToken() : cookieRefreshToken;
        authService.logout(tokenToUse);

        // Expire cookie
        ResponseCookie cookie = ResponseCookie.from(REFRESH_COOKIE_NAME, "")
            .httpOnly(true)
            .secure(true)
            .path("/api/v1/auth")
            .maxAge(0)
            .sameSite("Strict")
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    private void setRefreshCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_COOKIE_NAME, refreshToken)
            .httpOnly(true)
            .secure(true)
            .path("/api/v1/auth")
            .maxAge(30 * 24 * 60 * 60) // 30 days
            .sameSite("Strict")
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
