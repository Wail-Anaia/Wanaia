package com.wanaia.domain.user.service;

import com.wanaia.common.exception.BusinessException;
import com.wanaia.common.exception.ResourceNotFoundException;
import com.wanaia.domain.user.dto.AuthResponse;
import com.wanaia.domain.user.dto.LoginRequest;
import com.wanaia.domain.user.dto.RegisterRequest;
import com.wanaia.domain.user.model.RefreshToken;
import com.wanaia.domain.user.model.User;
import com.wanaia.domain.user.model.UserRole;
import com.wanaia.domain.user.model.UserStatus;
import com.wanaia.domain.user.repository.RefreshTokenRepository;
import com.wanaia.domain.user.repository.UserRepository;
import com.wanaia.domain.user.security.JwtTokenProvider;
import com.wanaia.domain.user.security.UserPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailAndDeletedAtIsNull(request.email())) {
            throw new BusinessException("EMAIL_ALREADY_EXISTS", "A user with email " + request.email() + " already exists.");
        }

        User user = new User(
            request.email().trim().toLowerCase(),
            passwordEncoder.encode(request.password()),
            request.firstName().trim(),
            request.lastName().trim(),
            UserRole.USER
        );

        if (request.phone() != null) user.setPhone(request.phone().trim());
        if (request.locale() != null) user.setLocale(request.locale());
        if (request.countryCode() != null) user.setCountryCode(request.countryCode());

        User saved = userRepository.save(user);
        UserPrincipal principal = UserPrincipal.create(saved);

        String accessToken = tokenProvider.generateAccessToken(principal);
        String refreshToken = createAndSaveRefreshToken(saved.getId(), "WEB", null, null);

        return buildAuthResponse(saved, accessToken, refreshToken);
    }

    @Transactional
    public AuthResponse login(LoginRequest request, String ipAddress) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email().trim().toLowerCase(), request.password())
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(principal.getId())
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", principal.getId()));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException("ACCOUNT_INACTIVE", "Your account is currently " + user.getStatus().name());
        }

        user.setLastLoginAt(Instant.now());
        userRepository.save(user);

        String accessToken = tokenProvider.generateAccessToken(principal);
        String clientType = (request.clientType() != null && request.clientType().equalsIgnoreCase("ANDROID")) ? "ANDROID" : "WEB";
        String refreshToken = createAndSaveRefreshToken(user.getId(), clientType, request.deviceInfo(), ipAddress);

        return buildAuthResponse(user, accessToken, refreshToken);
    }

    @Transactional
    public AuthResponse refreshAccessToken(String rawRefreshToken, String clientType) {
        if (rawRefreshToken == null || rawRefreshToken.trim().isEmpty()) {
            throw new BadCredentialsException("Refresh token is missing");
        }

        RefreshToken token = refreshTokenRepository.findByTokenHash(rawRefreshToken)
            .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));

        if (token.isExpired() || token.isRevoked()) {
            throw new BadCredentialsException("Refresh token is expired or revoked");
        }

        User user = userRepository.findById(token.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", token.getUserId()));

        // Rotate Refresh Token
        token.setRevokedAt(Instant.now());
        refreshTokenRepository.save(token);

        UserPrincipal principal = UserPrincipal.create(user);
        String newAccessToken = tokenProvider.generateAccessToken(principal);
        String newRefreshToken = createAndSaveRefreshToken(user.getId(), clientType != null ? clientType : token.getClientType(), token.getDeviceInfo(), token.getIpAddress());

        return buildAuthResponse(user, newAccessToken, newRefreshToken);
    }

    @Transactional
    public void logout(String rawRefreshToken) {
        if (rawRefreshToken != null && !rawRefreshToken.trim().isEmpty()) {
            refreshTokenRepository.findByTokenHash(rawRefreshToken).ifPresent(token -> {
                token.setRevokedAt(Instant.now());
                refreshTokenRepository.save(token);
            });
        }
    }

    private String createAndSaveRefreshToken(Long userId, String clientType, String deviceInfo, String ipAddress) {
        String tokenString = UUID.randomUUID().toString() + "-" + UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plus(30, ChronoUnit.DAYS);

        RefreshToken refreshToken = new RefreshToken(userId, tokenString, clientType, deviceInfo, ipAddress, expiresAt);
        refreshTokenRepository.save(refreshToken);
        return tokenString;
    }

    private AuthResponse buildAuthResponse(User user, String accessToken, String refreshToken) {
        AuthResponse.UserSummary summary = new AuthResponse.UserSummary(
            user.getId(),
            user.getUuid(),
            user.getEmail(),
            user.getDisplayName(),
            user.getRole().name(),
            user.getLocale(),
            user.getCountryCode()
        );

        return new AuthResponse(accessToken, refreshToken, tokenProvider.getAccessTokenExpirationMs(), summary);
    }
}
