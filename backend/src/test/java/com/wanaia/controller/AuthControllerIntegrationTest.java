package com.wanaia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanaia.domain.user.dto.LoginRequest;
import com.wanaia.domain.user.dto.RegisterRequest;
import com.wanaia.domain.user.dto.TokenRefreshRequest;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES)
@ActiveProfiles("test")
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Complete Web Auth Flow: Register -> Login (receives HttpOnly cookie) -> Refresh -> Logout")
    void shouldExecuteWebAuthLifecycle() throws Exception {
        String email = "web.test." + System.currentTimeMillis() + "@wanaia.com";
        RegisterRequest registerReq = new RegisterRequest(email, "StrongPassword123!", "Web", "User", "+212600000000", "fr-MA", "MA");

        // 1. Register
        MvcResult registerResult = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerReq)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data.accessToken", notNullValue()))
            .andExpect(header().exists("Set-Cookie"))
            .andReturn();

        Cookie refreshCookie = registerResult.getResponse().getCookie("wanaia_refresh_token");

        // 2. Login (Web client)
        LoginRequest loginReq = new LoginRequest(email, "StrongPassword123!", "WEB", "Chrome on Windows");
        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginReq)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data.accessToken", notNullValue()))
            .andExpect(header().exists("Set-Cookie"))
            .andReturn();

        Cookie loginCookie = loginResult.getResponse().getCookie("wanaia_refresh_token");
        assert loginCookie != null;

        // 3. Silent Refresh using HttpOnly cookie
        MvcResult refreshResult = mockMvc.perform(post("/api/v1/auth/refresh")
                .cookie(loginCookie))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data.accessToken", notNullValue()))
            .andExpect(header().exists("Set-Cookie"))
            .andReturn();

        Cookie rotatedCookie = refreshResult.getResponse().getCookie("wanaia_refresh_token");
        assert rotatedCookie != null;

        // 4. Logout
        mockMvc.perform(post("/api/v1/auth/logout")
                .cookie(rotatedCookie))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    @DisplayName("Complete Android Auth Flow: Login (receives refresh token in payload) -> Refresh via body")
    void shouldExecuteAndroidAuthLifecycle() throws Exception {
        String email = "android.test." + System.currentTimeMillis() + "@wanaia.com";
        RegisterRequest registerReq = new RegisterRequest(email, "AndroidPassword123!", "Android", "User", "+212611111111", "ar-MA", "MA");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerReq)))
            .andExpect(status().isOk());

        // 1. Android Login
        LoginRequest loginReq = new LoginRequest(email, "AndroidPassword123!", "ANDROID", "Samsung Galaxy S24");
        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginReq)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data.refreshToken", notNullValue()))
            .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        String refreshToken = objectMapper.readTree(responseBody).path("data").path("refreshToken").asText();

        // 2. Android Refresh using request body
        TokenRefreshRequest refreshReq = new TokenRefreshRequest(refreshToken, "ANDROID");
        mockMvc.perform(post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshReq)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data.accessToken", notNullValue()))
            .andExpect(jsonPath("$.data.refreshToken", notNullValue()));
    }
}
