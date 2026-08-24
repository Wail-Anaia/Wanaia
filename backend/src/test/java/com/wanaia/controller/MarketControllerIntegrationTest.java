package com.wanaia.controller;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES)
@ActiveProfiles("test")
public class MarketControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/v1/markets returns sovereign markets")
    void shouldReturnActiveMarkets() throws Exception {
        mockMvc.perform(get("/api/v1/markets"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(2))));
    }

    @Test
    @DisplayName("GET /api/v1/markets/{code} returns Morocco MAR market")
    void shouldReturnMarketByCode() throws Exception {
        mockMvc.perform(get("/api/v1/markets/MAR"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data.code", is("MAR")))
            .andExpect(jsonPath("$.data.currencyCode", is("MAD")));
    }

    @Test
    @DisplayName("GET /api/v1/markets/products/1 returns market pricing and fiscal CV")
    void shouldReturnMarketAvailabilities() throws Exception {
        mockMvc.perform(get("/api/v1/markets/products/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(2))))
            .andExpect(jsonPath("$.data[0].localTrimName", notNullValue()))
            .andExpect(jsonPath("$.data[0].msrpBasePrice", notNullValue()));
    }
}
