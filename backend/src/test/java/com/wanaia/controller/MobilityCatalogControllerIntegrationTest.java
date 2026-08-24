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
public class MobilityCatalogControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/v1/mobility/categories returns seeded categories")
    void shouldReturnMobilityCategories() throws Exception {
        mockMvc.perform(get("/api/v1/mobility/categories"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(2))))
            .andExpect(jsonPath("$.data[0].code", notNullValue()));
    }

    @Test
    @DisplayName("GET /api/v1/mobility/brands returns seeded brands")
    void shouldReturnBrands() throws Exception {
        mockMvc.perform(get("/api/v1/mobility/brands"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(4))));
    }

    @Test
    @DisplayName("GET /api/v1/mobility/brands/{slug} returns specific brand")
    void shouldReturnBrandBySlug() throws Exception {
        mockMvc.perform(get("/api/v1/mobility/brands/toyota"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data.name", is("Toyota")))
            .andExpect(jsonPath("$.data.countryOfOrigin", is("JPN")));
    }

    @Test
    @DisplayName("GET /api/v1/mobility/products/1 returns seeded product with powertrain assembly")
    void shouldReturnProductWithPowertrain() throws Exception {
        mockMvc.perform(get("/api/v1/mobility/products/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data.brandName", is("Toyota")))
            .andExpect(jsonPath("$.data.modelName", is("RAV4")))
            .andExpect(jsonPath("$.data.variantName", is("2.5 Hybrid AWD-i")))
            .andExpect(jsonPath("$.data.powertrain.propulsionType", is("HEV")))
            .andExpect(jsonPath("$.data.powertrain.combinedPowerHp", is(222)));
    }
}
