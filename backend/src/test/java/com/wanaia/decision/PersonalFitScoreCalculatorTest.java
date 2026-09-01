package com.wanaia.decision;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanaia.domain.decision.calculator.PersonalFitScoreCalculatorV1;
import com.wanaia.domain.decision.model.PersonalFitScoreInput;
import com.wanaia.domain.decision.model.PersonalFitScoreOutput;
import com.wanaia.domain.profile.model.MobilityProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class PersonalFitScoreCalculatorTest {

    private PersonalFitScoreCalculatorV1 calculator;

    @BeforeEach
    void setUp() {
        calculator = new PersonalFitScoreCalculatorV1(new ObjectMapper());
    }

    @Test
    @DisplayName("Evaluate vehicle perfectly matching profile constraints and preferences")
    void shouldScoreHighForMatchingProfile() {
        MobilityProfile profile = new MobilityProfile(1L, "MA", "MAD", BigDecimal.valueOf(500000.00));
        profile.setTypicalPassengerCount(4);
        profile.setPriorityReliability(5);
        profile.setPriorityFuelEconomy(4);
        profile.setHasHomeCharging(true);

        PersonalFitScoreInput input = new PersonalFitScoreInput(
            1L, "2.5 Hybrid AWD-i", "SUV", "HEV", BigDecimal.valueOf(415000.00),
            5.7, 5, 2, 580, 88.0, 8.1, profile
        );

        PersonalFitScoreOutput output = calculator.calculate(input);

        assertNotNull(output);
        assertTrue(output.meetsHardConstraints());
        assertTrue(output.violatedHardConstraints().isEmpty());
        assertTrue(output.fitScoreValue().doubleValue() >= 80.0);
        assertFalse(output.reasonsWhyFits().isEmpty());
    }

    @Test
    @DisplayName("Exclude or penalize vehicle violating hard constraints (EV with no charging access)")
    void shouldPenalizeEvWhenNoChargingAvailable() {
        MobilityProfile profile = new MobilityProfile(2L, "MA", "MAD", BigDecimal.valueOf(600000.00));
        profile.setHasHomeCharging(false);
        profile.setHasWorkCharging(false);

        PersonalFitScoreInput input = new PersonalFitScoreInput(
            3L, "Long Range", "SUV", "BEV", BigDecimal.valueOf(590000.00),
            16.9, 5, 2, 854, 80.0, 5.0, profile
        );

        PersonalFitScoreOutput output = calculator.calculate(input);

        assertNotNull(output);
        assertFalse(output.meetsHardConstraints());
        assertTrue(output.violatedHardConstraints().contains("NO_CHARGING_INFRASTRUCTURE"));
        assertTrue(output.fitScoreValue().doubleValue() < 55.0, "Score must drop heavily upon hard constraint violation");
        assertFalse(output.reasonsWhyDoesNotFit().isEmpty());
    }
}
