# WANAIA — API Architecture & Contract

## RESTful API v1 Specification & Integration Standards

---

## 1. Core API Principles

1. **Single Source of Truth:** One unified Spring Boot REST API for both Angular Web and Native Android clients.
2. **Strict Epistemic DTO Isolation:** JPA entities are **never** returned directly. MapStruct mappers transform entities into strictly typed Request and Response DTOs.
3. **OpenAPI as the Contract:** The API contract is generated and validated via OpenAPI 3.0 (`springdoc-openapi`).
4. **Predictable Envelopes & Error Handling:** Standardized response wrapper and RFC 7807 compliant error format.

---

## 2. API Response & Error Envelopes

### 2.1 Standard Success Envelope

```json
{
  "success": true,
  "data": { ... },
  "meta": {
    "page": 0,
    "size": 20,
    "totalElements": 1420,
    "totalPages": 71,
    "isLast": false
  },
  "timestamp": "2026-08-17T12:00:00Z"
}
```

### 2.2 Standard Error Envelope

```json
{
  "success": false,
  "error": {
    "code": "VEHICLE_NOT_FOUND",
    "message": "The requested vehicle variant does not exist in market MAR.",
    "status": 404,
    "timestamp": "2026-08-17T12:00:00Z",
    "path": "/api/v1/vehicles/1209",
    "validationErrors": [
      {
        "field": "marketCode",
        "rejectedValue": "XYZ",
        "message": "Must be a valid ISO-3166-1 alpha-3 code."
      }
    ]
  }
}
```

---

## 3. Comprehensive REST Endpoint Catalog

### 3.1 Authentication & Security (`/api/v1/auth`)
| Method | Path | Description | Access |
|---|---|---|---|
| `POST` | `/api/v1/auth/register` | User registration (Email/Phone) | Public |
| `POST` | `/api/v1/auth/login` | Login returning JWT access + refresh token | Public |
| `POST` | `/api/v1/auth/refresh` | Refresh expired access token | Public (Cookie/Body) |
| `POST` | `/api/v1/auth/logout` | Revoke active refresh token | Authenticated |
| `POST` | `/api/v1/auth/forgot-password`| Request password reset OTP/Email | Public |
| `POST` | `/api/v1/auth/reset-password` | Complete password reset | Public |

### 3.2 Mobility Knowledge & Catalog (`/api/v1/mobility`)
| Method | Path | Description | Access |
|---|---|---|---|
| `GET` | `/api/v1/mobility/categories` | List active mobility categories (Car, Moto, Van) | Public |
| `GET` | `/api/v1/mobility/brands` | List brands with category & market filters | Public |
| `GET` | `/api/v1/mobility/brands/{slug}` | Brand details, logo, country of origin | Public |
| `GET` | `/api/v1/mobility/models/{brandSlug}/{modelSlug}` | Model info, generations, active years | Public |
| `GET` | `/api/v1/mobility/products/{id}` | Complete Mobility Product master page data | Public |
| `GET` | `/api/v1/mobility/products/{id}/provenance` | Attribute-level data provenance & trust breakdown | Public |
| `GET` | `/api/v1/mobility/products/{id}/alternatives` | Direct competitors and segment alternatives | Public |

### 3.3 Decision Engine & Scores (`/api/v1/decisions`)
| Method | Path | Description | Access |
|---|---|---|---|
| `GET` | `/api/v1/decisions/global-score/{productId}` | Calculate/retrieve versioned Global WANAIA Score | Public |
| `POST` | `/api/v1/decisions/fit-score/{productId}` | Calculate Personal Fit Score against supplied profile | Public / Auth |
| `POST` | `/api/v1/decisions/tco` | Calculate customized multi-year TCO calculation | Public / Auth |
| `GET` | `/api/v1/decisions/deal-score/{listingId}` | Calculate Deal Score for a specific listing | Public |

### 3.4 Mobility Profile (`/api/v1/profiles`)
| Method | Path | Description | Access |
|---|---|---|---|
| `GET` | `/api/v1/profiles/me` | Fetch authenticated user's Mobility Profile | User |
| `PUT` | `/api/v1/profiles/me` | Create or update full Mobility Profile | User |
| `PATCH` | `/api/v1/profiles/me/priorities` | Update specific scoring weight preferences | User |

### 3.5 Search & Discovery (`/api/v1/search`)
| Method | Path | Description | Access |
|---|---|---|---|
| `POST` | `/api/v1/search/query` | Structured faceted search for catalog & listings | Public |
| `POST` | `/api/v1/search/natural-language`| Translate natural language prompt to criteria & search | Public |
| `GET` | `/api/v1/search/suggestions` | Typeahead autocomplete for brands, models, queries | Public |
| `GET` | `/api/v1/search/facets` | Dynamic facet counts based on current filters | Public |

### 3.6 Marketplace Listings (`/api/v1/listings`)
| Method | Path | Description | Access |
|---|---|---|---|
| `GET` | `/api/v1/listings` | Paginated listing feed with sorting & filters | Public |
| `GET` | `/api/v1/listings/{id}` | Full listing detail with trust badges & Deal Score | Public |
| `POST` | `/api/v1/listings` | Create a new vehicle listing | User / Dealer |
| `PUT` | `/api/v1/listings/{id}` | Update listing details | Owner / Admin |
| `DELETE`| `/api/v1/listings/{id}` | Soft delete listing | Owner / Admin |
| `POST` | `/api/v1/listings/{id}/favorite` | Add listing to user favorites | User |
| `DELETE`| `/api/v1/listings/{id}/favorite` | Remove listing from favorites | User |

### 3.7 WANAIA AI Advisor (`/api/v1/ai`)
| Method | Path | Description | Access |
|---|---|---|---|
| `POST` | `/api/v1/ai/chat` | Send message to AI Advisor with profile grounding | Public / User |
| `GET` | `/api/v1/ai/sessions/{sessionId}` | Retrieve conversational history | Public / User |
| `POST` | `/api/v1/ai/explain-comparison` | Generate structured comparison narrative | Public |

### 3.8 My Garage (`/api/v1/garage`)
| Method | Path | Description | Access |
|---|---|---|---|
| `GET` | `/api/v1/garage/vehicles` | List user's owned vehicles in garage | User |
| `POST` | `/api/v1/garage/vehicles` | Add owned vehicle to garage | User |
| `GET` | `/api/v1/garage/vehicles/{id}/expenses` | Expense log (maintenance, fuel, insurance) | User |
| `POST` | `/api/v1/garage/vehicles/{id}/expenses` | Log new expense item | User |

---

## 4. Client Integration Rules

1. **Angular Client:**
   - Interceptors automatically attach `Authorization: Bearer <token>` and `Accept-Language: {ar|fr|en}`.
   - Handles `401 Unauthorized` by triggering silent refresh via `/api/v1/auth/refresh`.
2. **Android Client:**
   - Retrofit client with OkHttp authenticator handling JWT refresh via encrypted tokens.
   - Network response models generated from OpenAPI schemas to ensure 100% alignment.

---

*This API contract ensures strict type-safety, maintainability, and clean integration between the backend core and all web and mobile clients.*
