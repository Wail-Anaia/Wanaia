package com.wanaia.domain.provenance.model;

public enum EpistemicType {
    FACT,                // Homologated, verifiable ground truth
    OBSERVATION,         // Real-world collected measurements (e.g. owner logs)
    CALCULATION,         // Deterministic formula output (e.g. power-to-weight)
    INTELLIGENCE,        // Synthesized statistical score (e.g. Deal Score)
    EDITORIAL_OPINION,   // Qualitative human road-test assessment
    USER_GENERATED,      // Subjective community ratings & feedback
    AI_EXPLANATION       // Conversational translation of structured facts
}
