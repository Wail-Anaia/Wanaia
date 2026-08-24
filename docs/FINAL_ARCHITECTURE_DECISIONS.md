# WANAIA — Final Architecture Decisions (Immutable Baseline)

---

## 1. Domain & Powertrain Hierarchy

### 1.1 Structural Decomposition
To prevent car-centric rigidity and accommodate all modern and future propulsion technologies, the domain strictly decouples identity, product definitions, physical powertrain assemblies, and market availability:

```
MobilityCategory (e.g. CAR, MOTORCYCLE, COMMERCIAL_VAN)
 └── Brand (e.g. Toyota, Yamaha, Renault)
      └── Model (e.g. RAV4, MT-07, Master)
           └── Generation (e.g. 5th Gen XA50 2018–2025)
                └── MobilityProduct / Variant
                     ├── Common Specifications & Category Attributes
                     └── PowertrainConfiguration (One-to-Many / Configurable)
                          ├── Internal Combustion Engine(s) [ICE] (Optional)
                          ├── Electric Motor(s) [Front/Rear/Hub] (Optional)
                          ├── Traction Battery Pack (Optional)
                          ├── Transmission / Reducer (Single-speed, e-CVT, Manual, DCT)
                          └── Energy Storage / Fuel System (Fuel tank, Charging ports)
```

### 1.2 Entity Responsibilities
- **`MobilityProduct`:** The global engineering/catalog definition of a vehicle variant (e.g., *RAV4 2.5L Hybrid AWD*).
- **`PowertrainConfiguration`:** The architectural assembly of power generation and delivery components. Supports pure ICE, Mild Hybrid (MHEV), Full Hybrid (HEV), Plug-in Hybrid (PHEV), Battery Electric (BEV), Range Extender (REEV), and Fuel Cell (FCEV).
- **`MarketAvailability`:** An independent aggregate capturing how a `MobilityProduct` is commercialized in a specific `Market` for a specific `ModelYear` (local trim naming, equipment packages, localized homologation specs, MSRP, fiscal CV, taxes, warranty).

---

## 2. DDD Aggregate Boundaries & Invariants

To avoid giant object graphs, memory bloat, and concurrency contention, the following aggregate boundaries are strictly enforced:

| Aggregate Root | Scope of Ownership & Atomic Consistency | Referenced Exclusively by ID | Must NOT be Loaded into Aggregate |
|---|---|---|---|
| **`MobilityCategory`** | Category code, multilingual metadata, category-specific spec schema definitions. | None | Brands, Models. |
| **`Brand`** | Brand identity, country of origin, logo, display order. | `categoryId` | Models, Products. |
| **`Model`** | Model identity, segment classification, active lifecycle status. | `brandId`, `categoryId` | Generations, Products. |
| **`Generation`** | Generation code, production start/end years, platform code, hero images. | `modelId` | Variants/Products. |
| **`MobilityProduct`** | Global variant name, body style, dimensions, curb weight, safety ratings. | `generationId`, `powertrainConfigId` | Market availabilities, listings, reviews. |
| **`PowertrainConfiguration`**| Component assembly mappings (engines, motors, battery, transmission), combined outputs. | Component IDs (`engineId`, `batteryId`, etc.) | MobilityProducts referencing it. |
| **`MarketAvailability`** | Localized commercial trim name, MSRP, standard/optional feature links, fiscal CV, road taxes, warranty terms. | `productId`, `marketId`, `modelYearId` | Product catalog trees, dealer inventories. |
| **`MarketplaceListing`** | Seller asking price, mileage, condition, location, verified trust flags, listing photos. | `userId`, `dealerId`, `marketAvailabilityId` | User profile graphs, dealer staff trees. |
| **`MobilityProfile`** | User mobility habits, commute distances, budget limits, priorities, category facets. | `userId` | User security credentials, garage vehicles. |
| **`ScoreResult`** | Immutable calculation output, rating class, confidence, explanation items. | `entityId`, `snapshotId` | Target entity object graphs. |
| **`ScoreInputSnapshot`** | Immutable, complete JSON payload of all inputs used during a score calculation. | `scoreResultId` | Live mutable catalog entities. |
| **`RecommendationTrace`**| Audit record of why vehicle candidates were ranked/recommended to a user at a given timestamp. | `userId`, `profileSnapshotId`, `candidateIds` | Live catalog or search indexes. |
| **`VehiclePassport`** | Official VIN identity, public homologated specs, access-controlled private service records. | `productId` | Public listing graphs. |

---

## 3. Market Availability & Global Localization

`MarketAvailability` is decoupled from the engineering product definition:
- The same `MobilityProduct` (e.g., *Toyota RAV4 2.5 Hybrid*) has distinct `MarketAvailability` records for Morocco (`MAR`), France (`FRA`), and UAE (`ARE`).
- **Morocco (`MAR`):** Sold as *Dynamic+*, priced in `MAD`, Fiscal Rating `8 CV`, annual vignette tax calculated via Moroccan tax schedules, 3-year/100,000 km warranty.
- **France (`FRA`):** Sold as *Lounge*, priced in `EUR`, subject to French CO2 *Malus Écologique*, 3-year/100,000 km warranty.

---

## 4. Epistemic Grounding & Data Provenance

1. **No "100% Fact" Absolutism:** WANAIA explicitly categorizes all data into 7 epistemic tiers (`FACT`, `OBSERVATION`, `CALCULATION`, `INTELLIGENCE`, `EDITORIAL_OPINION`, `USER_GENERATED`, `AI_EXPLANATION`).
2. **Confidence & Uncertainty:** Every score, TCO estimate, and owner observation carries a confidence metric ($0.00 \dots 1.00$) reflecting data density and source authority.
3. **AI Grounding Rule:** The WANAIA AI Advisor is strictly prohibited from inventing specifications, prices, or availability. Responses are constructed using attributable WANAIA structured data and must explicitly state when an attribute is estimated or unverified.

---

## 5. Score Reconstructability & Recommendation Traceability

Every score generated by the Decision Engine is immutable and auditable:
- **`ScoreInputSnapshot`:** Persists the complete, verbatim JSON input state at the millisecond of calculation.
- **`RecommendationTrace`:** Persists the exact user profile state, candidate pool, scoring formulas applied, weights, positive justification factors, and negative trade-off warnings.
- **Auditing Capability:** Any score or recommendation rendered in the past can be re-evaluated and explained deterministically.

---

## 6. Client-Specific Security Architecture

### 6.1 Web Client (Angular 18+ SSR/CSR)
- **Access Token:** Short-lived JWT (15 minutes) stored exclusively in browser memory (service variable / closure). Never in `localStorage` or `sessionStorage` (mitigating XSS extraction).
- **Refresh Token:** Stored in an `HttpOnly`, `Secure`, `SameSite=Strict` cookie managed entirely by the browser. Path-restricted to `/api/v1/auth/refresh` and `/api/v1/auth/logout`.
- **CSRF Protection:** SameSite cookie isolation combined with custom header validation (`X-Requested-With` / Anti-CSRF token).

### 6.2 Mobile Client (Native Android Java)
- **Access Token:** Short-lived JWT (15 minutes) held in memory during app lifecycle.
- **Refresh Token:** Stored securely in Android `EncryptedSharedPreferences` backed by the Android Keystore (AES-256 GCM encryption).
- **Refresh Flow:** OkHttp `Authenticator` intercepts `401 Unauthorized`, sends the refresh token via a secure Authorization header/body, updates encrypted storage, and replays the failed request.

---

## 7. Vehicle Passport Access Control & Privacy

The `/passport/:vin` endpoint is partitioned into two distinct security tiers:
1. **Public Vehicle Information (No Auth Required):**
   - Make, Model, Generation, Model Year, Homologated Technical Specifications, Safety Ratings.
2. **Private Vehicle Passport Records (Strict Authorization Required):**
   - Mileage logs, maintenance history, accident records, title transfers, battery health reports.
   - **Access Rules:** Only accessible by the **Verified Owner** (proven via uploaded registration document / "Carte Grise") or a third party granted **Time-Limited Digital Consent** (QR Code / SMS OTP authorization by the owner).

---

## 8. Internationalization & Locale Strategy

The platform abstracts locale as `{language}-{COUNTRY_CODE}` (e.g. `fr-MA`, `ar-MA`, `en-MA`, `fr-FR`, `en-US`):
- **Core Business Logic:** Consumes a `LocaleContext` containing `language`, `country`, `currency`, and `unitSystem` (Metric vs. Imperial).
- **URL Strategy:**
  - Default market locale: `/{lang-country}/...` or cleanly mapped localized route prefixes (e.g. `/fr-ma/cars/...`, `/ar-ma/cars/...`).
  - Business logic is completely decoupled from URL formatting.

---

## 9. Modular Monolith Architecture (No Premature Microservices or External Gateway)

- The application is a single, highly modular Spring Boot deployment.
- **API & Application Layer:** Managed by Spring MVC Controllers, Spring Security Filters, and Exception Advices. No separate external API Gateway infrastructure is required for MVP.
- **Inter-Module Communication:** Synchronous execution via Inbound Port Interfaces. Zero direct repository sharing between modules.

---

*These architectural decisions are immutable and govern all subsequent implementation phases.*
