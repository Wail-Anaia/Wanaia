package com.wanaia.decision;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanaia.domain.decision.calculator.TcoCalculatorV1;
import com.wanaia.domain.decision.model.MarketContext;
import com.wanaia.domain.decision.model.TcoInput;
import com.wanaia.domain.decision.model.TcoOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class TcoCalculatorTest {

    private TcoCalculatorV1 calculator;

    @BeforeEach
    void setUp() {
        calculator = new TcoCalculatorV1(new ObjectMapper());
    }

    @Test
    @DisplayName("Calculate 1, 3, and 5-year TCO for Moroccan market")
    void shouldCalculateMultiHorizonTcoForMorocco() {
        TcoInput input = new TcoInput(
            1L,
            BigDecimal.valueOf(415000.00),
            15000,
            5.7,
            "HEV",
            9,
            BigDecimal.valueOf(3000.00),
            MarketContext.morocco()
        );

        TcoOutput output = calculator.calculate(input);

        assertNotNull(output);
        assertEquals("MAD", output.currencyCode());
        assertEquals(3, output.horizons().size());

        TcoOutput.TcoHorizonBreakdown fiveYear = output.horizons().get(5);
        assertNotNull(fiveYear);
        assertEquals(5, fiveYear.horizonYears());
        assertTrue(fiveYear.totalTco().compareTo(BigDecimal.ZERO) > 0);
        assertTrue(fiveYear.monthlyAverageCost().compareTo(BigDecimal.ZERO) > 0);
        assertTrue(fiveYear.costPerKm().compareTo(BigDecimal.ZERO) > 0);
        assertEquals(0, fiveYear.estimatedResidualValue().compareTo(BigDecimal.valueOf(207500.00))); // 50% residual value at 5 years
    }
}
