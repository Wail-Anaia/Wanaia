# WANAIA — AI Architecture

## Grounded Intelligence & The WANAIA Agent

---

## 1. Grounding Mandate & Core Principle

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          THE WANAIA GROUNDING PIPELINE                      │
└─────────────────────────────────────────────────────────────────────────────┘
                                       │
 1. USER INTERACTION                   ▼
    "I need a reliable 7-seater SUV under 350,000 MAD for long family trips."
                                       │
 2. INTENT CLASSIFICATION              ▼
    Intent: [MOBILITY_ADVISORY] + [STRUCTURED_SEARCH]
                                       │
 3. PARAMETER EXTRACTION               ▼
    Criteria: { category: "CAR", body: "SUV", seats: 7, budgetMax: 350000, 
                market: "MAR", priority: "RELIABILITY" }
                                       │
 4. KNOWLEDGE & SEARCH RETRIEVAL       ▼
    Fetch matching canonical Mobility Products + Market Availabilities + Specs
                                       │
 5. DECISION ENGINE EXECUTION          ▼
    Calculate: [WANAIA Global Score] + [Personal Fit Score] + [5-Year TCO]
                                       │
 6. EXPLANATION ENGINE EXECUTION       ▼
    Generate deterministic Pros, Cons, and Trade-off matrices
                                       │
 7. AI CONVERSATIONAL SYNTHESIS        ▼
    LLM translates structured facts & scores into fluent, empathetic advice
    (in French, Arabic, or English based on user context)
                                       │
 8. FACT & BOUNDARY VALIDATION         ▼
    Verify that output contains no hallucinated prices, nonexistent trims, or
    disputed specs before emitting to client
                                       │
 9. FINAL CLIENT RESPONSE              ▼
    Render rich interactive cards (Specs + Scores + Deal Badges) + Chat text
```

---

## 2. AI Provider Decoupling & Java Contract

The backend interacts exclusively through a provider-neutral interface:

```java
package com.wanaia.intelligence.ai.provider;

public interface AiProvider {
    AiGenerationResult generateCompletion(AiPromptContext context);
    AiIntentResult classifyIntent(String userUtterance, String conversationHistoryJson);
    AiExtractionResult extractSearchCriteria(String naturalLanguageQuery);
    String getProviderIdentifier(); // "GEMINI", "OPENAI", "LOCAL_LLAMA"
}

public record AiPromptContext(
    String systemPrompt,
    String userMessage,
    List<AiMessageHistory> history,
    Map<String, Object> groundedStructuredData, // Exact specs, scores, and explanations
    Double temperature,
    Integer maxTokens
) {}
```

The application business logic has **zero direct dependency** on any proprietary SDK.

---

## 3. Strict Safety & Hallucination Prevention Rules

1. **Numerical Clamp:** If the LLM generates a numerical price or specification that deviates from the `groundedStructuredData`, the response validator intercepts and corrects the number or replaces the sentence.
2. **Missing Data Transparency:** If an attribute is unknown in WANAIA's database, the prompt directs the LLM to state: *"WANAIA does not currently have verified data for this specification."*
3. **No Financial or Legal Warranty:** All pricing and financing advice includes automated disclaimer metadata.

---

*This architecture guarantees that the WANAIA AI Advisor acts as an eloquent, helpful expert grounded 100% in verified WANAIA intelligence.*
