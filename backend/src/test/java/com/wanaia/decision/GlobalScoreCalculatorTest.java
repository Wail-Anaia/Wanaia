package com.wanaia.decision;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanaia.domain.decision.calculator.AlgorithmVersion;
import com.wanaia.domain.decision.calculator.GlobalScoreCalculatorV1;
import com.wanaia.domain.decision.model.GlobalScoreInput;
import com.wanaia.domain.decision.model.GlobalScoreOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalScoreCalculatorTest {

    private GlobalScoreCalculatorV1 calculator;

    @BeforeEach
    void setUp() {
        calculator = new GlobalScoreCalculatorV1(new ObjectMapper());
    }

    @Test
    @DisplayName("Calculate Global WANAIA Score for modern Hybrid SUV")
    void shouldCalculateGlobalScoreForHybridSuv() {
        GlobalScoreInput input = new GlobalScoreInput(
            1L,
            "2.5 Hybrid AWD-i",
            "SUV",
            "HEV",
            BigDecimal.valueOf(415000.00),
            5.0,  // NCAP 5 stars
            88.0, // High reliability
            5.7,  // 5.7 L/100km
            222,
            8.1,
            580,  // Expansive boot
            5,
            0.70, // 70% resale
            Collections.emptyMap()
        );

        GlobalScoreOutput output = calculator.calculate(input);

        assertNotNull(output);
        assertEquals(AlgorithmVersion.GLOBAL_SCORE_V1, output.algorithmVersion());
        assertTrue(output.scoreValue().doubleValue() >= 80.0, "High spec hybrid SUV should score >= 80");
        assertEquals("GOOD", output.ratingClass());
        assertTrue(output.confidenceLevel().doubleValue() >= 0.90);
        assertFalse(output.explanations().isEmpty());
        assertNotNull(output.snapshotHash());
    }

    @Test
    @DisplayName("Deterministic Reproducibility: Same input must yield identical score and snapshot hash")
    void shouldProduceIdenticalResultsForIdenticalInputs() {
        GlobalScoreInput input = new GlobalScoreInput(
            2L, "TCe 90", "HATCHBACK", "ICE", BigDecimal.valueOf(165000.00),
            4.0, 75.0, 5.2, 90, 12.2, 391, 5, 0.62, Collections.emptyMap()
        );

        GlobalScoreOutput run1 = calculator.calculate(input);
        GlobalScoreOutput run2 = calculator.calculate(input);

        assertEquals(run1.scoreValue(), run2.scoreValue());
        assertEquals(run1.ratingClass(), run2.ratingClass());
        assertEquals(run1.snapshotHash(), run2.snapshotHash());
        assertEquals(run1.rawSnapshotJson(), run2.rawSnapshotJson());
    }

    @Test
    @DisplayName("Handle missing data gracefully without crashing or zero-defaulting")
    void shouldHandleMissingDataGracefully() {
        GlobalScoreInput input = new GlobalScoreInput(
            99L, "Sparse Variant", "SEDAN", "ICE", BigDecimal.valueOf(200000.00),
            null, null, null, null, null, null, null, null, Collections.emptyMap()
        );

        GlobalScoreOutput output = calculator.calculate(input);

        assertNotNull(output);
        assertTrue(output.scoreValue().doubleValue() > 50.0 && output.scoreValue().doubleValue() < 80.0);
        assertTrue(output.confidenceLevel().doubleValue() <= 0.75, "Confidence should drop when data is sparse");
    }
}
