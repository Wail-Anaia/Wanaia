# WANAIA — Technical Architecture

## System Overview & Global Mobility Architecture

---

## 1. System Architecture Diagram

```
┌──────────────────────────────────────────────────────────────────────────────┐
│                              CLIENT PRESENTATION LAYER                       │
│                                                                              │
│    ┌───────────────────────────┐            ┌───────────────────────────┐    │
│    │     Angular 18+ Web       │            │   Native Android Studio   │    │
│    │   (SSR/CSR, Signals,      │            │   (Java, Jetpack, MVVM,   │    │
│    │    SCSS, i18n, RTL/LTR)   │            │    Room, Retrofit, FCM)   │    │
│    └─────────────┬─────────────┘            └─────────────┬─────────────┘    │
│                  │                                        │                  │
└──────────────────┼────────────────────────────────────────┼──────────────────┘
                   │                                        │
                   │           HTTPS / REST API v1          │
                   │         (Strict OpenAPI Contract)      │
                   │                                        │
┌──────────────────▼────────────────────────────────────────▼──────────────────┐
│                         SPRING BOOT MODULAR MONOLITH                         │
│                                                                              │
│   ┌──────────────────────────────────────────────────────────────────────┐   │
│   │                      APPLICATION PORTS & SECURITY                    │   │
│   │   [JWT Auth Filter] [Rate Limiter] [CORS] [Locale] [Audit Log AOP]   │   │
│   └──────────────────────────────────┬───────────────────────────────────┘   │
│                                      │                                       │
│   ┌──────────────────────────────────▼───────────────────────────────────┐   │
│   │                         DOMAIN MODULES                               │   │
│   │                                                                      │   │
│   │  ┌───────────────────────┐  ┌───────────────────────┐  ┌──────────┐  │   │
│   │  │ Mobility Product      │  │ Decision Engine       │  │ User &   │  │   │
│   │  │ (Catalog, Specs, Gen) │  │ (Scores, TCO, Deals)  │  │ Profile  │  │   │
│   │  └───────────────────────┘  └───────────────────────┘  └──────────┘  │   │
│   │  ┌───────────────────────┐  ┌───────────────────────┐  ┌──────────┐  │   │
│   │  │ Marketplace Listings  │  │ Provenance & Trust    │  │ Search   │  │   │
│   │  │ (Verified, TrustScore)│  │ (Facts, Sources, Audit│  │ (FTS+NLP)│  │   │
│   │  └───────────────────────┘  └───────────────────────┘  └──────────┘  │   │
│   │  ┌───────────────────────┐  ┌───────────────────────┐  ┌──────────┐  │   │
│   │  │ Dealer Management     │  │ AI Advisor Grounding  │  │ Garage   │  │   │
│   │  │ (Leads, Best Offer)   │  │ (Prompt, Context, Val)│  │ (Owners) │  │   │
│   │  └───────────────────────┘  └───────────────────────┘  └──────────┘  │   │
│   │                                                                      │   │
│   └──────────────────────────────────┬───────────────────────────────────┘   │
│                                      │                                       │
│   ┌──────────────────────────────────▼───────────────────────────────────┐   │
│   │                       INFRASTRUCTURE & ADAPTERS                      │   │
│   │   ┌──────────────┐   ┌──────────────┐   ┌──────────────┐             │   │
│   │   │ Redis L2     │   │ Object Store │   │ AI Provider  │             │   │
│   │   │ (Cache/Locks)│   │ (S3/MinIO)   │   │ (Abstracted) │             │   │
│   │   └──────────────┘   └──────────────┘   └──────────────┘             │   │
│   └──────────────────────────────────┬───────────────────────────────────┘   │
│                                      │                                       │
│   ┌──────────────────────────────────▼───────────────────────────────────┐   │
│   │                             PostgreSQL                               │   │
│   │                  (Primary System of Record, v16+)                    │   │
│   └──────────────────────────────────────────────────────────────────────┘   │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Hexagonal / Ports & Adapters Architecture Rule

To maintain strict domain boundaries and prevent spaghetti dependencies:
1. **Modules communicate exclusively through Inbound Interfaces (Ports).**
2. **A module NEVER directly injects or calls another module's JPA Repository or Entity.**
3. Example: The `MarketplaceListingService` calls `MobilityProductQueryPort.findProductSummary(id)` rather than injecting `ProductRepository`.

```java
// Example Port Definition
package com.wanaia.domain.mobility.port.inbound;

public interface MobilityProductQueryPort {
    Optional<MobilityProductSummaryDto> findProductSummary(Long productId);
    boolean existsById(Long productId);
    MarketAvailabilityDto getMarketAvailability(Long productId, String marketCode);
}
```

---

## 3. Technology Stack & Verification Matrix

| Component | Technology | Version | Purpose & Architectural Justification |
|---|---|---|---|
| **Backend Runtime** | Java | 21 LTS | High-throughput, virtual threads, strong static typing, long-term enterprise stability. |
| **Backend Framework** | Spring Boot | 3.3+ | Industry standard enterprise framework, Spring Security, Spring Data JPA. |
| **Database** | PostgreSQL | 16+ | Robust ACID compliance, relational integrity, JSONB support, Full-Text Search. |
| **Migration Engine** | Flyway | Latest | Version-controlled, reproducible, immutable database migrations. |
| **Web Frontend** | Angular | 18+ | Enterprise modularity, built-in SSR for SEO, Signals for reactivity, strict TypeScript. |
| **Mobile Application** | Native Android (Java) | Android 8.0+ (API 26+) | Native performance, Retrofit network layer, Room caching, Jetpack Navigation. |
| **Caching Layer** | Redis | 7+ | Distributed caching, session tokens, rate limiting counters. |
| **API Contract** | OpenAPI (Swagger) | 3.0 | Strict schema definition ensuring synchronization across Web and Android. |

---

*This architecture guarantees strict enterprise decoupling, zero direct client-database coupling, and full mobility-first flexibility.*
