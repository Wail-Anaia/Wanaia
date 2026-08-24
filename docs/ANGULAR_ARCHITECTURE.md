# WANAIA — Angular Frontend Architecture

## Modern Mobility Web Application

---

## 1. Domain Models & Mobility-First Typing

```typescript
// src/app/core/models/mobility.model.ts

export type MobilityCategoryCode = 'CAR' | 'MOTORCYCLE' | 'SCOOTER' | 'VAN' | 'TRUCK' | 'E_BIKE';

export interface MobilityCategory {
  id: number;
  code: MobilityCategoryCode;
  name: string;
  iconUrl: string;
}

export interface Brand {
  id: number;
  categoryId: number;
  name: string;
  slug: string;
  logoUrl: string;
  countryOfOrigin: string;
}

export interface MobilityProduct {
  id: number;
  uuid: string;
  brandName: string;
  modelName: string;
  generationName: string;
  trimName: string;
  slug: string;
  modelYear: number;
  category: MobilityCategoryCode;
  bodyType: string;
  
  // Normalized Powertrain
  powertrain: {
    type: string;
    fuel: string;
    powerHp: number;
    powerKw: number;
    torqueNm: number;
    consumptionWltp: number;
    co2EmissionsGkm: number;
    batteryKwh?: number;
    evRangeKm?: number;
  };
  
  // Market Localization (Current Market Context)
  marketContext: {
    marketCode: string;
    msrpBasePrice: number;
    currencyCode: string;
    fiscalRatingCv?: number;
    warrantyYears: number;
    warrantyKm: number;
  };
  
  // WANAIA Decision Scores
  scores: {
    globalWanaiaScore: number;
    personalFitScore?: number;
    reliabilityIndex: number;
    dealScore?: number;
  };
}

export interface ScoreExplanation {
  type: 'PRO' | 'CON' | 'WARNING';
  category: string;
  code: string;
  message: string;
}
```

---

## 2. Shared Mobility Design System Components

The frontend design system provides purpose-built, reusable components:
- `w-score-badge`: Circular, high-contrast WANAIA Score badge with rating-specific chromatic gradients.
- `w-fit-indicator`: Visual comparison between Global Score vs. User Personal Fit Score.
- `w-deal-badge`: Real-time market valuation badge (EXCELLENT, GOOD, FAIR, EXPENSIVE).
- `w-provenance-tooltip`: Epistemic trust popup showing exact data origin and verification status.
- `w-price-tag`: Currency-aware, market-formatted price indicator (e.g. `340 000 MAD`).

---

## 3. SEO & Server-Side Rendering (Angular SSR)

SSR is strictly enabled for all public catalog, listing, brand, and editorial routes. Hydration is optimized using Angular Signals and `@defer (on viewport)` for below-the-fold media galleries and owner reviews.

---

*This architecture guarantees lightning-fast Core Web Vitals, mobile-first responsiveness, and total domain alignment with WANAIA's mobility intelligence.*
