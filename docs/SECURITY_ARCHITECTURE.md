# WANAIA — Security Architecture

## Client-Specific Security & Vehicle Passport Privacy

---

## 1. Client-Specific Authentication Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          AUTHENTICATION FLOW BY CLIENT                      │
└─────────────────────────────────────────────────────────────────────────────┘

 WEB CLIENT (Angular 18+ SSR/CSR)
 ────────────────────────────────
 1. POST /api/v1/auth/login ──────────▶ Backend Authenticates Credentials
 2. Response:
    - Body: { accessToken: "eyJ...", expiresIn: 900 } (Stored in Memory ONLY)
    - Set-Cookie: refresh_token="xyz..."; HttpOnly; Secure; SameSite=Strict; Path=/api/v1/auth
 3. Subsequent API Requests:
    - Header: Authorization: Bearer <accessToken>
 4. Token Refresh (Silent):
    - POST /api/v1/auth/refresh (Browser automatically sends HttpOnly Cookie)
    - Backend rotates refresh token cookie and issues new in-memory accessToken

 ANDROID CLIENT (Native Java)
 ────────────────────────────
 1. POST /api/v1/auth/login ──────────▶ Backend Authenticates Credentials
 2. Response:
    - Body: { accessToken: "eyJ...", refreshToken: "xyz...", expiresIn: 900 }
 3. Storage:
    - accessToken in Application Memory
    - refreshToken in Android EncryptedSharedPreferences (AES-256 GCM + Keystore)
 4. Subsequent API Requests:
    - Header: Authorization: Bearer <accessToken>
 5. Token Refresh:
    - OkHttp Authenticator catches 401 Unauthorized
    - POST /api/v1/auth/refresh with body { refreshToken: "<stored_token>" }
    - Updates EncryptedSharedPreferences and replays failed request
```

---

## 2. Vehicle Passport Privacy & Access Control

The `/passport/:vin` endpoint is partitioned into two distinct security tiers:

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       VEHICLE PASSPORT PRIVACY MODEL                        │
├───────────────────────────────┬─────────────────────────────────────────────┤
│ Tier 1: Public Vehicle Info   │ Tier 2: Private Vehicle Passport History    │
│ (Open Access / SEO Indexable) │ (Strict Owner Authorization Required)       │
├───────────────────────────────┼─────────────────────────────────────────────┤
│ • Make, Model, Generation     │ • Full Title & Registration History         │
│ • Model Year & Homologation   │ • Exact Odometer Reading Logs               │
│ • Official Technical Specs    │ • Maintenance & Workshop Invoices           │
│ • Crash Safety Test Ratings   │ • Insurance Accident Claim History          │
│ • Standard Equipment List     │ • Battery Health & Degradation Reports      │
│ • Market Valuation Range      │ • Vehicle Inspection Diagnostic Scans       │
└───────────────────────────────┴─────────────────────────────────────────────┘
```

### 2.1 Authorization & Consent Matrix:
1. **Registered Owner:** Has full read/write/upload access to their vehicle's passport upon providing verified registration documents ("Carte Grise").
2. **Third-Party Buyer / Dealership:** Can access Tier 2 Private Passport data **only** when the registered owner grants **Time-Limited Digital Consent** (via an encrypted QR Code or an SMS-verified OTP authorization).
3. **Anonymized VIN Lookups:** Entering a VIN without consent yields **only Tier 1 Public Vehicle Info** and a status indicator confirming whether a verified Tier 2 passport exists.

---

## 3. Role-Based Access Control (RBAC)

```java
public enum UserRole {
    USER,           // Public buyer/seller, garage owner, reviewer
    DEALER,         // Showroom staff, inventory manager
    DEALER_ADMIN,   // Dealership owner, branch manager
    EDITOR,         // Automotive journalist, content author
    MODERATOR,      // Review & listing moderation, abuse prevention
    SUPPORT,        // Customer service agent (limited PII read)
    ANALYST,        // Market intelligence & reporting viewer
    ADMIN,          // System administrator, catalog curator
    SUPER_ADMIN     // Platform security & root configuration
}
```

---

*This architecture guarantees strict token isolation tailored to platform capabilities and enforces enterprise-grade privacy protection on all vehicle data.*
