package com.wanaia.domain.decision.calculator;

import com.wanaia.domain.decision.model.ExplanationType;
import com.wanaia.domain.decision.model.ScoreExplanationItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExplanationEngineV1 {

    public List<ScoreExplanationItem> synthesizeExplanations(
        List<ScoreExplanationItem> globalExplanations,
        List<ScoreExplanationItem> fitExplanations,
        List<ScoreExplanationItem> tcoExplanations
    ) {
        List<ScoreExplanationItem> combined = new ArrayList<>();
        if (fitExplanations != null) {
            combined.addAll(fitExplanations);
        }
        if (globalExplanations != null) {
            combined.addAll(globalExplanations);
        }
        if (tcoExplanations != null) {
            combined.addAll(tcoExplanations);
        }
        return combined;
    }

    public String generateComparisonRationale(String leaderName, double leaderScore, String challengerName, double challengerScore, String distinctAdvantage) {
        return String.format(
            "%s ranks higher than %s (Overall Fit %.1f vs %.1f) due to %s.",
            leaderName, challengerName, leaderScore, challengerScore, distinctAdvantage
        );
    }
}
