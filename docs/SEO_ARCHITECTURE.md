# WANAIA — SEO Architecture

## Search Engine Optimization Strategy

---

## 1. SEO Principles

1. **SEO is a first-class architectural requirement**, not an afterthought
2. Every public page must be indexable with complete, unique metadata
3. Server-side rendering (SSR) for all SEO-critical pages
4. Structured data (Schema.org) on every relevant page
5. Clean, hierarchical, human-readable URLs
6. No thin or duplicate content
7. Fast page loads (Core Web Vitals)
8. Mobile-first indexing readiness

---

## 2. Angular SSR Strategy

### Pages Requiring SSR

| Page Type | Route Pattern | Priority |
|-----------|--------------|----------|
| Homepage | `/` | Critical |
| Brand listing | `/cars` | Critical |
| Brand page | `/cars/:brand` | Critical |
| Model page | `/cars/:brand/:model` | Critical |
| Vehicle page | `/cars/:brand/:model/:gen` | Critical |
| Trim page | `/cars/:brand/:model/:gen/:trim` | Critical |
| Listing detail | `/listings/:id` | High |
| Dealer profile | `/dealers/:slug` | High |
| Article | `/news/:slug` | High |
| Guide | `/guides/:slug` | High |
| Search results | `/search?...` | Medium |
| Comparison | `/compare/:id` | Medium |

### Pages NOT Requiring SSR (CSR Only)

| Page Type | Reason |
|-----------|--------|
| Login/Register | Not indexed |
| My Garage | Authenticated content |
| Account settings | Authenticated content |
| Admin dashboard | Authenticated content |
| AI Chat | Dynamic interaction |

---

## 3. URL Architecture

### Principles
- Hierarchical and descriptive
- Lowercase with hyphens
- No query parameters for primary content
- Locale prefix for non-default languages

### URL Patterns

```
# Vehicles (primary content)
/cars                                    → All car brands
/cars/toyota                             → Toyota brand page
/cars/toyota/rav4                        → Toyota RAV4 model page
/cars/toyota/rav4/2024-xa50              → RAV4 5th gen page
/cars/toyota/rav4/2024-xa50/hybrid-xle   → Specific trim

# Marketplace
/listings                                → Browse listings
/listings/12345-2023-toyota-rav4-hybrid  → SEO-friendly listing URL

# Dealers
/dealers                                 → Browse dealers
/dealers/autocenter-casablanca           → Dealer profile

# Content
/news                                    → News hub
/news/2026-toyota-rav4-refresh           → Article
/guides                                  → Guides hub
/guides/best-family-suv-2026             → Guide

# Comparisons
/compare/toyota-rav4-vs-honda-cr-v       → Named comparison

# Best Of
/best-of/family-car                      → Best Of lists
/best-of/electric-suv

# EV (future)
/ev                                      → EV hub
/ev/charging-map                         → Charging infrastructure
/ev/range-calculator                     → Range tools

# Motorcycles (future)
/motorcycles                             → Motorcycle hub
/motorcycles/yamaha/mt-07               → Motorcycle model

# i18n
/ar/cars/toyota/rav4                     → Arabic version
/en/cars/toyota/rav4                     → English version
(default = French, no prefix)
```

---

## 4. Metadata Strategy

### 4.1 Dynamic Title Tags

| Page | Title Pattern |
|------|--------------|
| Homepage | `WANAIA — Plateforme d'Intelligence Mobilité` |
| Brand | `{Brand} — Modèles, Prix et Avis | WANAIA` |
| Model | `{Brand} {Model} {Year} — Prix, Fiche Technique, Avis | WANAIA` |
| Trim | `{Brand} {Model} {Trim} {Year} — Prix à partir de {Price} | WANAIA` |
| Listing | `{Year} {Brand} {Model} — {Price} MAD — {City} | WANAIA` |
| Article | `{Article Title} | WANAIA` |
| Comparison | `{Car1} vs {Car2} — Comparaison Complète | WANAIA` |

### 4.2 Meta Descriptions

Auto-generated from structured data with templates per page type:

```typescript
// Vehicle page example
const description = `Découvrez le ${brand} ${model} ${year}. ` +
  `Prix à partir de ${price} MAD. ` +
  `Moteur ${engine}, ${power} ch, consommation ${consumption} L/100km. ` +
  `Score WANAIA: ${score}/10. Comparez, trouvez des offres.`;
```

### 4.3 Open Graph Tags

```html
<meta property="og:title" content="Toyota RAV4 Hybrid 2025">
<meta property="og:description" content="Prix, fiche technique...">
<meta property="og:image" content="https://wanaia.com/images/toyota-rav4-2025.jpg">
<meta property="og:url" content="https://wanaia.com/cars/toyota/rav4/2025-xa50">
<meta property="og:type" content="product">
<meta property="og:site_name" content="WANAIA">
<meta property="og:locale" content="fr_MA">
<meta property="og:locale:alternate" content="ar_MA">
<meta property="og:locale:alternate" content="en_US">
```

---

## 5. Structured Data (Schema.org)

### 5.1 Vehicle Page

```json
{
  "@context": "https://schema.org",
  "@type": "Car",
  "name": "Toyota RAV4 Hybrid XLE 2025",
  "brand": {
    "@type": "Brand",
    "name": "Toyota"
  },
  "model": "RAV4",
  "vehicleModelDate": "2025",
  "fuelType": "https://schema.org/HybridFuel",
  "vehicleEngine": {
    "@type": "EngineSpecification",
    "fuelType": "Hybrid"
  },
  "offers": {
    "@type": "AggregateOffer",
    "lowPrice": "380000",
    "highPrice": "450000",
    "priceCurrency": "MAD",
    "offerCount": 15
  },
  "aggregateRating": {
    "@type": "AggregateRating",
    "ratingValue": "4.5",
    "reviewCount": "127",
    "bestRating": "5"
  }
}
```

### 5.2 Listing (Product)

```json
{
  "@context": "https://schema.org",
  "@type": "Car",
  "name": "2023 Toyota RAV4 Hybrid",
  "offers": {
    "@type": "Offer",
    "price": "385000",
    "priceCurrency": "MAD",
    "availability": "https://schema.org/InStock",
    "seller": {
      "@type": "AutoDealer",
      "name": "AutoCenter Casablanca"
    }
  },
  "mileageFromOdometer": {
    "@type": "QuantitativeValue",
    "value": "25000",
    "unitCode": "KMT"
  }
}
```

### 5.3 Article

```json
{
  "@context": "https://schema.org",
  "@type": "Article",
  "headline": "Les meilleurs SUV familiaux 2026",
  "author": {
    "@type": "Organization",
    "name": "WANAIA"
  },
  "datePublished": "2026-08-01",
  "image": "https://wanaia.com/images/best-family-suv.jpg"
}
```

### 5.4 Breadcrumb

```json
{
  "@context": "https://schema.org",
  "@type": "BreadcrumbList",
  "itemListElement": [
    { "@type": "ListItem", "position": 1, "item": { "@id": "/cars", "name": "Voitures" }},
    { "@type": "ListItem", "position": 2, "item": { "@id": "/cars/toyota", "name": "Toyota" }},
    { "@type": "ListItem", "position": 3, "item": { "@id": "/cars/toyota/rav4", "name": "RAV4" }}
  ]
}
```

---

## 6. Sitemap Strategy

### XML Sitemaps

```
/sitemap.xml                → Sitemap index
/sitemap-brands.xml         → All brand pages
/sitemap-models.xml         → All model pages
/sitemap-vehicles.xml       → All vehicle/trim pages
/sitemap-listings.xml       → Active marketplace listings
/sitemap-dealers.xml        → All dealer profiles
/sitemap-articles.xml       → Published articles
/sitemap-guides.xml         → Published guides
/sitemap-pages.xml          → Static pages
```

### Generation Strategy

- Generated by Spring Boot backend: `GET /sitemap.xml`
- Dynamic based on database content
- `lastmod` from entity `updated_at`
- Listings sitemap regenerated frequently (listings change often)
- Vehicle/brand sitemaps regenerated on data changes
- Maximum 50,000 URLs per sitemap file

### robots.txt

```
User-agent: *
Allow: /
Disallow: /admin/
Disallow: /account/
Disallow: /garage/
Disallow: /auth/
Disallow: /api/

Sitemap: https://wanaia.com/sitemap.xml
```

---

## 7. Internal Linking

### Automatic Cross-Links

| From | To | Example |
|------|----|---------|
| Brand page | Models, articles, listings | Toyota → RAV4, Corolla |
| Model page | Trims, comparisons, reviews | RAV4 → Hybrid XLE, vs CR-V |
| Vehicle page | Similar vehicles, listings, articles | RAV4 → CR-V, Tucson |
| Article | Referenced vehicles, brands | "Best SUVs" → RAV4, CR-V links |
| Listing | Vehicle page, dealer, similar | Listing → RAV4 page |
| Comparison | Both vehicle pages, listings | RAV4 vs CR-V → both pages |

---

## 8. Performance (Core Web Vitals)

| Metric | Target | Strategy |
|--------|--------|----------|
| LCP | < 2.5s | SSR, optimized images, font preloading |
| FID/INP | < 200ms | Lazy loading, code splitting, minimal JS |
| CLS | < 0.1 | Reserved image dimensions, stable layouts |

### Implementation

```html
<!-- Preload critical resources -->
<link rel="preload" href="/fonts/wanaia-primary.woff2" as="font" crossorigin>
<link rel="preconnect" href="https://api.wanaia.com">

<!-- Lazy load below-fold images -->
<img loading="lazy" src="..." width="400" height="300" alt="...">

<!-- Responsive images -->
<img srcset="vehicle-300w.webp 300w,
             vehicle-600w.webp 600w,
             vehicle-1200w.webp 1200w"
     sizes="(max-width: 600px) 300px, (max-width: 1200px) 600px, 1200px"
     alt="Toyota RAV4 2025">
```

---

## 9. Canonical URLs

```typescript
// Prevent duplicate content
// Always set canonical to the definitive URL
this.seoService.setCanonicalUrl(
  `https://wanaia.com/cars/${brand.slug}/${model.slug}/${generation.slug}`
);

// For paginated listing results, canonical points to page 1
// For filtered search results, canonical includes primary filters only
```

---

## 10. Hreflang (Multi-Language)

```html
<link rel="alternate" hreflang="fr" href="https://wanaia.com/cars/toyota/rav4">
<link rel="alternate" hreflang="ar" href="https://wanaia.com/ar/cars/toyota/rav4">
<link rel="alternate" hreflang="en" href="https://wanaia.com/en/cars/toyota/rav4">
<link rel="alternate" hreflang="x-default" href="https://wanaia.com/cars/toyota/rav4">
```

---

*SEO is built into the Angular architecture from the ground up — not retrofitted. Every public page has proper metadata, structured data, canonical URLs, and SSR.*
