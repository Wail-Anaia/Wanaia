package com.wanaia.decision;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanaia.domain.decision.calculator.DealScoreCalculatorV1;
import com.wanaia.domain.decision.model.DealRating;
import com.wanaia.domain.decision.model.DealScoreInput;
import com.wanaia.domain.decision.model.DealScoreOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class DealScoreCalculatorTest {

    private DealScoreCalculatorV1 calculator;

    @BeforeEach
    void setUp() {
        calculator = new DealScoreCalculatorV1(new ObjectMapper());
    }

    @Test
    @DisplayName("Classify EXCELLENT_DEAL when priced > 8% below adjusted market estimate")
    void shouldClassifyExcellentDeal() {
        DealScoreInput input = new DealScoreInput(
            "listing-101", 1L,
            BigDecimal.valueOf(320000.00), // Asking price
            BigDecimal.valueOf(370000.00), // Base estimate
            12,                            // Sample size >= 5
            40000, 60000, 3, "EXCELLENT", "MAD"
        );

        DealScoreOutput output = calculator.calculate(input);

        assertNotNull(output);
        assertEquals(DealRating.EXCELLENT_DEAL, output.rating());
        assertEquals(BigDecimal.valueOf(95.00), output.dealScoreValue());
        assertTrue(output.priceDifferencePercentage().doubleValue() < -8.0);
    }

    @Test
    @DisplayName("Return INSUFFICIENT_DATA when market sample size < 5")
    void shouldReturnInsufficientDataWhenSampleSizeSmall() {
        DealScoreInput input = new DealScoreInput(
            "listing-102", 1L,
            BigDecimal.valueOf(350000.00),
            BigDecimal.valueOf(360000.00),
            3, // Small sample size (< 5)
            50000, 50000, 2, "GOOD", "MAD"
        );

        DealScoreOutput output = calculator.calculate(input);

        assertNotNull(output);
        assertEquals(DealRating.INSUFFICIENT_DATA, output.rating());
        assertNull(output.dealScoreValue());
        assertEquals(BigDecimal.valueOf(0.30), output.confidenceLevel());
    }
}
