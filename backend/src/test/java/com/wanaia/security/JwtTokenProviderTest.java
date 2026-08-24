package com.wanaia.security;

import com.wanaia.domain.user.model.User;
import com.wanaia.domain.user.model.UserRole;
import com.wanaia.domain.user.security.JwtTokenProvider;
import com.wanaia.domain.user.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;
    private static final String TEST_SECRET = "TestSecretKeyForWanaiaJwtAuthenticationMustBeAtLeast256BitsLongForHmacSha256!";
    private static final long EXPIRATION_MS = 60000; // 1 minute

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider(TEST_SECRET, EXPIRATION_MS);
    }

    @Test
    @DisplayName("Should generate valid JWT access token and extract subject")
    void shouldGenerateAndValidateToken() {
        User user = new User("wail@wanaia.com", "hash", "Wail", "Anaia", UserRole.ADMIN);
        user.setId(42L);

        UserPrincipal principal = UserPrincipal.create(user);
        String token = tokenProvider.generateAccessToken(principal);

        assertNotNull(token);
        assertTrue(tokenProvider.validateToken(token));
        assertEquals(42L, tokenProvider.getUserIdFromJwt(token));
    }

    @Test
    @DisplayName("Should reject invalid or malformed token")
    void shouldRejectInvalidToken() {
        assertFalse(tokenProvider.validateToken("invalid.jwt.token"));
        assertFalse(tokenProvider.validateToken(""));
        assertFalse(tokenProvider.validateToken(null));
    }
}
