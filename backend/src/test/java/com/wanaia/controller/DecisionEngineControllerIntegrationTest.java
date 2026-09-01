package com.wanaia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanaia.domain.decision.model.DealScoreInput;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES)
@ActiveProfiles("test")
public class DecisionEngineControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/v1/decisions/global-score/1 calculates and persists score result and explanations")
    void shouldCalculateAndPersistGlobalScore() throws Exception {
        mockMvc.perform(get("/api/v1/decisions/global-score/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data.algorithmVersion", is("GLOBAL_SCORE_V1")))
            .andExpect(jsonPath("$.data.scoreValue", notNullValue()))
            .andExpect(jsonPath("$.data.snapshotHash", notNullValue()))
            .andExpect(jsonPath("$.data.dimensionScores.reliability", notNullValue()))
            .andExpect(jsonPath("$.data.explanations", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @DisplayName("GET /api/v1/decisions/personal-fit/1 calculates personal fit matching profile")
    void shouldCalculatePersonalFitScore() throws Exception {
        mockMvc.perform(get("/api/v1/decisions/personal-fit/1?userId=1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data.algorithmVersion", is("PERSONAL_FIT_V1")))
            .andExpect(jsonPath("$.data.fitScoreValue", notNullValue()))
            .andExpect(jsonPath("$.data.meetsHardConstraints", is(true)));
    }

    @Test
    @DisplayName("POST /api/v1/decisions/deal-score evaluates market valuation")
    void shouldCalculateDealScore() throws Exception {
        DealScoreInput dealInput = new DealScoreInput(
            "listing-777", 1L,
            BigDecimal.valueOf(390000.00),
            BigDecimal.valueOf(410000.00),
            8,
            30000, 45000, 2, "EXCELLENT", "MAD"
        );

        mockMvc.perform(post("/api/v1/decisions/deal-score")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dealInput)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data.rating", is("EXCELLENT_DEAL")))
            .andExpect(jsonPath("$.data.dealScoreValue", is(95.0)));
    }

    @Test
    @DisplayName("GET /api/v1/decisions/tco/1 returns 1/3/5-year horizons")
    void shouldCalculateTco() throws Exception {
        mockMvc.perform(get("/api/v1/decisions/tco/1?marketCode=MAR"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data.algorithmVersion", is("TCO_V1")))
            .andExpect(jsonPath("$.data.currencyCode", is("MAD")))
            .andExpect(jsonPath("$.data.horizons.5.totalTco", notNullValue()));
    }

    @Test
    @DisplayName("GET /api/v1/decisions/recommendations ranks candidates and saves trace")
    void shouldGenerateRankedRecommendationsAndTrace() throws Exception {
        mockMvc.perform(get("/api/v1/decisions/recommendations?userId=1&marketCode=MAR"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data.algorithmVersion", is("RECOMMENDATION_V1")))
            .andExpect(jsonPath("$.data.rankedRecommendations", hasSize(greaterThanOrEqualTo(2))))
            .andExpect(jsonPath("$.data.rankedRecommendations[0].rank", is(1)))
            .andExpect(jsonPath("$.data.comparisonRationale", notNullValue()));
    }
}
