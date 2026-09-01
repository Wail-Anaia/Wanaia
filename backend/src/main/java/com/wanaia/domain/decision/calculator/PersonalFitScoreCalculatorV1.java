package com.wanaia.domain.decision.calculator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanaia.domain.decision.model.*;
import com.wanaia.domain.decision.util.SnapshotHasher;
import com.wanaia.domain.profile.model.MobilityProfile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class PersonalFitScoreCalculatorV1 {

    private final ObjectMapper objectMapper;

    public PersonalFitScoreCalculatorV1(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public PersonalFitScoreOutput calculate(PersonalFitScoreInput input) {
        if (input == null || input.userProfile() == null) {
            throw new IllegalArgumentException("PersonalFitScoreInput and userProfile must not be null");
        }

        MobilityProfile profile = input.userProfile();
        List<String> violatedHardConstraints = new ArrayList<>();
        List<ScoreExplanationItem> reasonsWhyFits = new ArrayList<>();
        List<ScoreExplanationItem> reasonsWhyDoesNotFit = new ArrayList<>();
        Map<String, BigDecimal> preferenceMatches = new LinkedHashMap<>();

        // ==========================================
        // 1. HARD CONSTRAINTS EVALUATION
        // ==========================================

        // A. Maximum Budget Hard Constraint (allow 10% flexible ceiling, beyond that is hard violation)
        if (profile.getBudgetMax() != null && input.localPrice() != null) {
            BigDecimal maxAllowed = profile.getBudgetMax().multiply(BigDecimal.valueOf(1.10));
            if (input.localPrice().compareTo(maxAllowed) > 0) {
                violatedHardConstraints.add("BUDGET_EXCEEDED");
                reasonsWhyDoesNotFit.add(new ScoreExplanationItem(null, ExplanationType.WARNING, "BUDGET", "EXCEEDS_MAX_BUDGET",
                    "Price exceeds maximum budget ceiling (" + input.localPrice() + " vs max " + profile.getBudgetMax() + ").", null, "USER_PROFILE"));
            } else if (input.localPrice().compareTo(profile.getBudgetMax()) <= 0) {
                reasonsWhyFits.add(new ScoreExplanationItem(null, ExplanationType.PRO, "BUDGET", "WITHIN_BUDGET",
                    "Priced comfortably within user stated budget limit.", null, "USER_PROFILE"));
            }
        }

        // B. Passenger Capacity Hard Constraint
        int requiredPassengers = (profile.getTypicalPassengerCount() != null) ? profile.getTypicalPassengerCount() : 1;
        int vehicleSeats = (input.seatCount() != null) ? input.seatCount() : 5;
        if (vehicleSeats < requiredPassengers) {
            violatedHardConstraints.add("INSUFFICIENT_SEAT_COUNT");
            reasonsWhyDoesNotFit.add(new ScoreExplanationItem(null, ExplanationType.WARNING, "CAPACITY", "INSUFFICIENT_SEATS",
                "Vehicle provides " + vehicleSeats + " seats, which is below the required " + requiredPassengers + " passengers.", null, "USER_PROFILE"));
        } else {
            reasonsWhyFits.add(new ScoreExplanationItem(null, ExplanationType.PRO, "CAPACITY", "ADEQUATE_SEATS",
                "Easily accommodates user passenger requirements (" + vehicleSeats + " seats).", null, "USER_PROFILE"));
        }

        // C. EV Charging Infrastructure Constraint
        if ("BEV".equalsIgnoreCase(input.propulsionType())) {
            boolean hasHome = Boolean.TRUE.equals(profile.getHasHomeCharging());
            boolean hasWork = Boolean.TRUE.equals(profile.getHasWorkCharging());
            if (!hasHome && !hasWork) {
                violatedHardConstraints.add("NO_CHARGING_INFRASTRUCTURE");
                reasonsWhyDoesNotFit.add(new ScoreExplanationItem(null, ExplanationType.WARNING, "CHARGING", "NO_CHARGING_ACCESS",
                    "Pure electric vehicle not recommended without home or workplace charging access.", null, "USER_PROFILE"));
            } else if (hasHome) {
                reasonsWhyFits.add(new ScoreExplanationItem(null, ExplanationType.PRO, "CHARGING", "HOME_CHARGING_COMPATIBLE",
                    "Home charging access unlocks maximum convenience and lowest energy cost for this EV.", null, "USER_PROFILE"));
            }
        }

        // ==========================================
        // 2. SOFT PREFERENCES MATCHING (1-5 scales)
        // ==========================================

        // A. Daily Commute & Efficiency Match
        int pFuel = (profile.getPriorityFuelEconomy() != null) ? profile.getPriorityFuelEconomy() : 3;
        double effScore = 70.0;
        if (input.fuelConsumptionMetric() != null) {
            if ("BEV".equalsIgnoreCase(input.propulsionType()) || "HEV".equalsIgnoreCase(input.propulsionType()) || "PHEV".equalsIgnoreCase(input.propulsionType())) {
                effScore = 95.0;
            } else if (input.fuelConsumptionMetric() <= 5.5) {
                effScore = 85.0;
            } else if (input.fuelConsumptionMetric() <= 7.5) {
                effScore = 70.0;
            } else {
                effScore = 50.0;
            }
        }
        BigDecimal effMatch = BigDecimal.valueOf(effScore).setScale(2, RoundingMode.HALF_UP);
        preferenceMatches.put("efficiencyMatch", effMatch);

        // B. Reliability Match
        int pRel = (profile.getPriorityReliability() != null) ? profile.getPriorityReliability() : 4;
        double relVal = (input.reliabilityScore() != null) ? input.reliabilityScore() : 75.0;
        BigDecimal relMatch = BigDecimal.valueOf(relVal).setScale(2, RoundingMode.HALF_UP);
        preferenceMatches.put("reliabilityMatch", relMatch);

        // C. Performance Match
        int pPerf = (profile.getPriorityPerformance() != null) ? profile.getPriorityPerformance() : 3;
        double perfVal = 70.0;
        if (input.acceleration0100s() != null && input.acceleration0100s() <= 7.0) {
            perfVal = 90.0;
        } else if (input.acceleration0100s() != null && input.acceleration0100s() <= 10.0) {
            perfVal = 75.0;
        } else {
            perfVal = 60.0;
        }
        BigDecimal perfMatch = BigDecimal.valueOf(perfVal).setScale(2, RoundingMode.HALF_UP);
        preferenceMatches.put("performanceMatch", perfMatch);

        // Compute Weighted Soft Preference Score
        double weightTotal = pFuel + pRel + pPerf;
        if (weightTotal == 0) weightTotal = 1.0;

        double weightedFit = (effScore * pFuel + relVal * pRel + perfVal * pPerf) / weightTotal;

        // Apply Hard Constraint Penalties
        boolean meetsHard = violatedHardConstraints.isEmpty();
        if (!meetsHard) {
            // Apply heavy 40% penalty per hard violation to ensure ranking separation
            weightedFit = Math.max(10.0, weightedFit - (violatedHardConstraints.size() * 40.0));
        }

        BigDecimal finalFitScore = BigDecimal.valueOf(weightedFit).setScale(2, RoundingMode.HALF_UP);

        // Snapshot Hash
        String rawJson;
        try {
            rawJson = objectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            rawJson = "{\"productId\":" + input.productId() + "}";
        }
        String snapshotHash = SnapshotHasher.sha256Hex(rawJson);

        return new PersonalFitScoreOutput(
            input.productId(),
            AlgorithmVersion.PERSONAL_FIT_V1,
            finalFitScore,
            meetsHard,
            violatedHardConstraints,
            preferenceMatches,
            reasonsWhyFits,
            reasonsWhyDoesNotFit,
            rawJson,
            snapshotHash
        );
    }
}
