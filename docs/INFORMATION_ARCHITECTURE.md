# WANAIA — Information Architecture

## Global Mobility Platform Navigation & Taxonomy

---

## 1. Information Architecture Principles

1. **Mobility-First Hierarchy:** Cars are the primary launch category, but the taxonomy gracefully scales to Motorcycles, Commercial Vehicles, and Micromobility.
2. **Dual-Path User Journeys:**
   - **Intent-Driven / Advisory Path:** "I need a vehicle for my family under 300,000 MAD" $\to$ AI Advisor / Guided Wizard / Fit Score.
   - **Research & Specification Path:** Category $\to$ Brand $\to$ Model $\to$ Generation $\to$ Trim / Spec.
3. **Multi-Lingual & Bidirectional:** Full URL and navigation symmetry across French (`/`), Arabic (`/ar/`), and English (`/en/`).
4. **Clean, SEO-Engineered Canonical Routes:** Deterministic, keyword-rich slugs without ugly internal ID fragments.

---

## 2. Complete Web Route Taxonomy

```
/                                           # Homepage: Hero Search, Advisory, Trending, Best-Of
├── /cars                                   # Passenger Cars Hub
│   ├── /new                                # New Cars & Catalog
│   ├── /used                               # Pre-owned Cars (Marketplace)
│   ├── /electric                           # EV & Hybrid Intelligence
│   ├── /compare                            # Side-by-Side Car Comparator
│   ├── /:brandSlug                         # e.g. /cars/toyota
│   │   └── /:modelSlug                     # e.g. /cars/toyota/rav4
│   │       └── /:genSlug                   # e.g. /cars/toyota/rav4/2024-xa50 (Vehicle Master Page)
│   │           └── /:trimSlug              # e.g. /cars/toyota/rav4/2024-xa50/hybrid-dynamic
│
├── /motorcycles                            # Two-Wheeler & Micromobility Hub
│   ├── /scooters                           # Urban Commuting & 125cc
│   ├── /electric                           # Electric Bikes & Scooters
│   ├── /compare                            # Motorcycle Comparator
│   └── /:brandSlug/:modelSlug              # e.g. /motorcycles/yamaha/mt-07
│
├── /commercial                             # Commercial & Fleet Mobility
│   ├── /vans                               # Light Commercial Vehicles
│   ├── /trucks                             # Heavy & Regional Transport
│   └── /compare                            # Utility Fleet Comparator
│
├── /search                                 # Universal Smart Search (Filters + Facets + NLP)
│
├── /listings                               # Marketplace Classifieds
│   ├── /:id-:slug                          # Listing Detail (e.g. /listings/48912-2023-toyota-rav4-casablanca)
│   └── /new                                # Post a Listing (Authenticated User/Dealer)
│
├── /dealers                                # Certified Dealer Directory
│   └── /:dealerSlug                        # Dealer Showroom & Verified Inventory
│
├── /best-offers                            # WANAIA Best Offer & Buyer Requests
│   ├── /requests/new                       # Post a Buyer Request ("Inverted Marketplace")
│   └── /requests/:id                       # Buyer Request Status & Received Dealer Offers
│
├── /passport                               # WANAIA Vehicle Passport (Digital History by VIN)
│   └── /:vinOrCode                         # Verified Vehicle Passport Report
│
├── /compare                                # Multi-Vehicle Comparator Engine (2–4 vehicles)
│
├── /ai                                     # WANAIA Agent (Interactive Mobility Advisor)
│
├── /garage                                 # My Garage (Ownership Management, Maintenance, Expenses)
│   ├── /vehicles/new                       # Add Owned Vehicle
│   └── /vehicles/:id                       # Vehicle Dashboard (Reminders, Odometer, Documents)
│
├── /profile                                # My Mobility Profile (Context, Budget, Commute, Priorities)
│
├── /editorial                              # Content & Knowledge Hub
│   ├── /news                               # Industry News & Launches
│   ├── /reviews                            # Expert & Road Test Reviews
│   ├── /guides                             # Buying & Maintenance Guides
│   └── /best-of                            # Editorial "Best of" Rankings (e.g. /editorial/best-of/family-suv)
│
├── /community                              # Vehicle-Centric Discussion & Owner Experience Forums
│   └── /:brandSlug/:modelSlug              # e.g. /community/toyota/rav4
│
├── /auth                                   # Authentication & Onboarding
│   ├── /login
│   ├── /register
│   ├── /forgot-password
│   └── /verify
│
└── /admin                                  # Operational Admin Console (RBAC Protected)
    ├── /dashboard
    ├── /mobility-products
    ├── /dealers
    ├── /listings
    ├── /reviews
    ├── /ai-analytics
    └── /audit-logs
```

---

## 3. Mobile (Android) Information Architecture

The Native Android App implements a clean Single-Activity architecture backed by Jetpack Navigation Component:

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                             BOTTOM NAVIGATION                               │
├──────────────┬──────────────┬──────────────┬───────────────┬────────────────┤
│ 1. Discover  │ 2. Search    │ 3. WANAIA AI │ 4. My Garage  │ 5. Account     │
│ (Home Hub)   │ (Listings &  │ (Advisory    │ (Ownership &  │ (Profile &     │
│              │  Catalog)    │  Assistant)  │  Maintenance) │  Favorites)    │
└──────────────┴──────────────┴──────────────┴───────────────┴────────────────┘
```

### Deep-Linking Hierarchy:
- `https://wanaia.com/cars/{brand}/{model}/{gen}` $\to$ Opens native `VehiclePageFragment`
- `https://wanaia.com/listings/{id}` $\to$ Opens native `ListingDetailFragment`
- `https://wanaia.com/dealers/{slug}` $\to$ Opens native `DealerProfileFragment`
- `https://wanaia.com/passport/{vin}` $\to$ Opens native `VehiclePassportFragment`

---

*This information architecture guarantees an intuitive, scalable, SEO-dominant structure across Web and Mobile.*
