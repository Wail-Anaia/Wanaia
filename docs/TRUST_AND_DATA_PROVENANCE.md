# WANAIA — Trust & Data Provenance Framework

## The Epistemic Integrity System

---

## 1. Vision & Mandate

WANAIA's competitive advantage is **absolute trust**. 
In the automotive and mobility sector, misinformation, manipulated reviews, hidden damages, and exaggerated marketing claims are pervasive.

WANAIA establishes a strict **Epistemic Data Classification System** where every piece of data rendered to a user, processed by the Decision Engine, or consumed by the AI Advisor is explicitly tagged with its classification, source, validity period, and confidence level.

---

## 2. Epistemic Data Hierarchy

```
┌────────────────────────────────────────────────────────────────────────┐
│                        EPISTEMIC DATA HIERARCHY                        │
├─────────────────────────┬──────────────────────────────────────────────┤
│ Classification          │ Definition & Concrete Examples               │
├─────────────────────────┼──────────────────────────────────────────────┤
│ 1. FACT                 │ Verifiable, immutable ground truth.          │
│                         │ • Engine displacement = 1,998 cc             │
│                         │ • Wheelbase = 2,690 mm                       │
│                         │ • Official homologated WLTP CO2 = 124 g/km   │
│                         │ • VIN Number = VF1...                        │
├─────────────────────────┼──────────────────────────────────────────────┤
│ 2. OBSERVATION          │ Empirically collected real-world data points.│
│                         │ • Owner-reported fuel consumption = 6.4 L/100│
│                         │ • Verified odometer reading at test = 42,100 │
│                         │ • Measured 10-80% DC charging time = 32 mins │
├─────────────────────────┼──────────────────────────────────────────────┤
│ 3. CALCULATION          │ Deterministic mathematical derivative.       │
│                         │ • Estimated 5-year TCO = 142,500 MAD         │
│                         │ • Power-to-weight ratio = 112 hp/tonne       │
│                         │ • Projected annual fuel cost at current rates│
├─────────────────────────┼──────────────────────────────────────────────┤
│ 4. INTELLIGENCE         │ Synthesized statistical score or benchmark.  │
│                         │ • WANAIA Global Score: 88/100 (v1.0)         │
│                         │ • Market Deal Score: EXCELLENT DEAL (92/100) │
│                         │ • Reliability Index: High (Top 15% in class) │
├─────────────────────────┼──────────────────────────────────────────────┤
│ 5. EDITORIAL OPINION    │ Qualitative human expert judgment.           │
│                         │ • WANAIA Chief Editor Road Test Verdict      │
│                         │ • "Best suited for highway cruising"         │
├─────────────────────────┼──────────────────────────────────────────────┤
│ 6. USER GENERATED (UGC) │ Subjective user reviews and community posts. │
│                         │ • Owner rating: 4.5/5                        │
│                         │ • User review text: "Infotainment is clunky" │
├─────────────────────────┼──────────────────────────────────────────────┤
│ 7. AI EXPLANATION       │ Natural language synthesis of Layers 1–6.    │
│                         │ • "We recommend the Hybrid over the Diesel   │
│                         │   because your 15 km daily commute will..."  │
└─────────────────────────┴──────────────────────────────────────────────┘
```

---

## 3. Provenance Data Model & Schema

Every critical domain record in WANAIA implements or references a `DataProvenance` entity.

### 3.1 Database Schema Representation

```sql
CREATE TABLE data_sources (
    id                  BIGSERIAL PRIMARY KEY,
    code                VARCHAR(50) NOT NULL UNIQUE,     -- e.g. 'OEM_TOYOTA_MA', 'EURO_NCAP', 'WANAIA_ROAD_TEST'
    name                VARCHAR(150) NOT NULL,
    source_type         VARCHAR(30) NOT NULL,            -- 'MANUFACTURER', 'REGULATOR', 'INDEPENDENT_TEST', 'DEALER', 'COMMUNITY'
    trust_tier          INTEGER NOT NULL DEFAULT 1,      -- 1 = Authority (highest), 2 = Verified Partner, 3 = Community
    website_url         VARCHAR(500),
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE attribute_provenance (
    id                  BIGSERIAL PRIMARY KEY,
    entity_type         VARCHAR(50) NOT NULL,            -- 'TRIM_SPECIFICATION', 'LISTING_PRICE', 'BATTERY_SPEC'
    entity_id           BIGINT NOT NULL,
    attribute_name      VARCHAR(100) NOT NULL,
    epistemic_type      VARCHAR(30) NOT NULL,            -- 'FACT', 'OBSERVATION', 'CALCULATION', 'INTELLIGENCE', 'EDITORIAL_OPINION'
    source_id           BIGINT REFERENCES data_sources(id),
    source_reference    VARCHAR(255),                    -- e.g. "Homologation Sheet #2026-MA-771"
    collected_at        TIMESTAMPTZ NOT NULL,
    valid_from          DATE NOT NULL,
    valid_to            DATE,
    market_code         VARCHAR(3) NOT NULL,             -- e.g. 'MAR' (Morocco)
    verification_status VARCHAR(30) NOT NULL,            -- 'OFFICIALLY_VERIFIED', 'AUDITED', 'DEALER_SUBMITTED', 'ESTIMATED'
    confidence_level    VARCHAR(20) NOT NULL DEFAULT 'HIGH', -- 'CONFIRMED_HIGH', 'MEDIUM', 'INDICATIVE'
    methodology_ref     VARCHAR(200),
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_attr_provenance_lookup ON attribute_provenance(entity_type, entity_id, attribute_name);
```

---

## 4. UI Trust Indicators & Transparency Standards

In both the Angular Web and Native Android applications, the user interface enforces transparency:

1. **Information Tooltips ("Where did this number come from?"):**
   - Tapping or hovering on any key metric (e.g. *Fuel Consumption: 5.2 L/100km*) reveals:
     - **Epistemic Class:** *Observation (Real-World Aggregated)*
     - **Source:** *42 Verified WANAIA Owner Logs (Morocco)*
     - **Confidence:** *High (Sample size > 30)*
     - **Official Homologated WLTP:** *4.8 L/100km (Manufacturer Fact)*
2. **Never Display "Verified" Fraudulently:**
   - A listing or dealer is only marked `VERIFIED` if physical documents (Registration Card / "Carte Grise", National ID, Dealer Commercial Register) have passed the verification workflow.
3. **Clear Labelling of Estimates:**
   - Any calculation (e.g. 5-year maintenance cost) must display an `[ESTIMATE]` indicator accompanied by a link to the calculation methodology.

---

## 5. Enforcement in the AI Layer

When the **WANAIA AI Advisor** constructs conversational answers, it is injected with the provenance tags:

```json
{
  "attribute": "battery_warranty",
  "value": "8 years / 160,000 km",
  "epistemic_type": "FACT",
  "source": "Official Manufacturer Warranty Book 2026",
  "verification_status": "OFFICIALLY_VERIFIED"
}
```

The AI generation prompt enforces:
- When presenting `FACT` $\to$ State definitively.
- When presenting `CALCULATION` or `INTELLIGENCE` $\to$ State as "WANAIA calculates..." or "Our Deal Score indicates...".
- When presenting `OBSERVATION` $\to$ State as "Based on real-world owner reports...".
- When data is missing or `ESTIMATED` $\to$ Disclose the estimation boundary transparently.

---

*WANAIA's trust architecture ensures that the platform is authoritative, legally compliant, and genuinely empowering for buyers, owners, and dealers.*
