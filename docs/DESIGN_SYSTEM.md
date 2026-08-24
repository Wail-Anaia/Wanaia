# WANAIA — Design System

## Premium Mobility Design Language

---

## 1. Design Philosophy

WANAIA's design language is:

- **Intelligent** — Data-rich but never overwhelming
- **Premium** — Feels like a luxury product, not a classified ads site
- **Trustworthy** — Clean, professional, confidence-inspiring
- **Editorial** — Beautiful content presentation like a premium magazine
- **Fast** — Performance is a design feature
- **Inclusive** — Accessible, RTL-native, multi-language

---

## 2. Color System

### 2.1 Core Palette

```scss
// Brand Colors
$wanaia-primary:        hsl(222, 80%, 52%);    // Electric Blue — trust, intelligence
$wanaia-primary-light:  hsl(222, 80%, 65%);
$wanaia-primary-dark:   hsl(222, 80%, 40%);

$wanaia-accent:         hsl(165, 70%, 45%);    // Emerald — success, growth
$wanaia-accent-light:   hsl(165, 70%, 60%);

// Semantic Colors
$color-success:         hsl(145, 63%, 42%);
$color-warning:         hsl(38, 92%, 50%);
$color-error:           hsl(0, 72%, 51%);
$color-info:            hsl(210, 79%, 46%);

// Neutral Palette
$gray-50:               hsl(220, 20%, 98%);
$gray-100:              hsl(220, 17%, 95%);
$gray-200:              hsl(220, 15%, 90%);
$gray-300:              hsl(220, 13%, 80%);
$gray-400:              hsl(220, 11%, 65%);
$gray-500:              hsl(220, 9%, 50%);
$gray-600:              hsl(220, 11%, 38%);
$gray-700:              hsl(220, 15%, 26%);
$gray-800:              hsl(220, 20%, 16%);
$gray-900:              hsl(220, 25%, 10%);
$gray-950:              hsl(220, 30%, 6%);

// Background
$bg-primary:            hsl(220, 20%, 99%);
$bg-secondary:          hsl(220, 17%, 96%);
$bg-elevated:           #FFFFFF;
$bg-dark:               $gray-900;

// Text
$text-primary:          $gray-900;
$text-secondary:        $gray-600;
$text-tertiary:         $gray-400;
$text-inverse:          #FFFFFF;
```

### 2.2 Deal Score Colors

```scss
$deal-excellent:        hsl(145, 70%, 40%);    // Deep green
$deal-good:             hsl(145, 55%, 50%);    // Green
$deal-fair:             hsl(38, 80%, 50%);     // Amber
$deal-expensive:        hsl(15, 70%, 50%);     // Orange
$deal-very-expensive:   hsl(0, 65%, 50%);      // Red
```

### 2.3 WANAIA Score Colors

```scss
$score-excellent:       hsl(145, 70%, 40%);    // 9-10
$score-great:           hsl(165, 60%, 42%);    // 8-9
$score-good:            hsl(200, 60%, 45%);    // 7-8
$score-average:         hsl(38, 80%, 50%);     // 5-7
$score-below:           hsl(15, 70%, 50%);     // 3-5
$score-poor:            hsl(0, 65%, 50%);      // 1-3
```

---

## 3. Typography

### 3.1 Font Stack

```scss
// Primary: Inter (Latin) + Noto Sans Arabic (Arabic)
$font-primary:       'Inter', 'Noto Sans Arabic', -apple-system, BlinkMacSystemFont, sans-serif;
$font-mono:          'JetBrains Mono', 'Fira Code', monospace;
$font-editorial:     'Outfit', 'Inter', sans-serif;  // For headlines
```

### 3.2 Type Scale

```scss
$text-xs:     0.75rem;     // 12px — Captions, metadata
$text-sm:     0.875rem;    // 14px — Secondary text, labels
$text-base:   1rem;        // 16px — Body text
$text-lg:     1.125rem;    // 18px — Lead text
$text-xl:     1.25rem;     // 20px — Section headers
$text-2xl:    1.5rem;      // 24px — Card titles
$text-3xl:    1.875rem;    // 30px — Page titles
$text-4xl:    2.25rem;     // 36px — Hero subtitles
$text-5xl:    3rem;        // 48px — Hero headlines
$text-6xl:    3.75rem;     // 60px — Display text

$line-height-tight:    1.2;
$line-height-normal:   1.5;
$line-height-relaxed:  1.75;

$font-weight-normal:   400;
$font-weight-medium:   500;
$font-weight-semibold: 600;
$font-weight-bold:     700;
$font-weight-extrabold:800;
```

---

## 4. Spacing System

```scss
$space-0:    0;
$space-1:    0.25rem;   // 4px
$space-2:    0.5rem;    // 8px
$space-3:    0.75rem;   // 12px
$space-4:    1rem;      // 16px
$space-5:    1.25rem;   // 20px
$space-6:    1.5rem;    // 24px
$space-8:    2rem;      // 32px
$space-10:   2.5rem;    // 40px
$space-12:   3rem;      // 48px
$space-16:   4rem;      // 64px
$space-20:   5rem;      // 80px
$space-24:   6rem;      // 96px
```

---

## 5. Border Radius

```scss
$radius-none:  0;
$radius-sm:    0.25rem;   // 4px — Chips, small elements
$radius-md:    0.5rem;    // 8px — Buttons, inputs
$radius-lg:    0.75rem;   // 12px — Cards
$radius-xl:    1rem;      // 16px — Modal, larger cards
$radius-2xl:   1.5rem;    // 24px — Feature cards
$radius-full:  9999px;    // Pills, avatars
```

---

## 6. Shadows

```scss
$shadow-xs:     0 1px 2px rgba(0, 0, 0, 0.05);
$shadow-sm:     0 1px 3px rgba(0, 0, 0, 0.1), 0 1px 2px rgba(0, 0, 0, 0.06);
$shadow-md:     0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px rgba(0, 0, 0, 0.06);
$shadow-lg:     0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px rgba(0, 0, 0, 0.05);
$shadow-xl:     0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px rgba(0, 0, 0, 0.04);
$shadow-2xl:    0 25px 50px -12px rgba(0, 0, 0, 0.25);
$shadow-inner:  inset 0 2px 4px rgba(0, 0, 0, 0.06);

// Elevation for cards
$shadow-card:        $shadow-sm;
$shadow-card-hover:  $shadow-lg;
$shadow-modal:       $shadow-2xl;
$shadow-dropdown:    $shadow-lg;
```

---

## 7. Breakpoints

```scss
$breakpoint-xs:   0;        // Mobile small
$breakpoint-sm:   576px;    // Mobile large
$breakpoint-md:   768px;    // Tablet
$breakpoint-lg:   992px;    // Desktop
$breakpoint-xl:   1200px;   // Large desktop
$breakpoint-2xl:  1440px;   // Extra large

// Container max-widths
$container-sm:    540px;
$container-md:    720px;
$container-lg:    960px;
$container-xl:    1140px;
$container-2xl:   1320px;
```

---

## 8. Component Library

### 8.1 Buttons

| Variant | Use Case |
|---------|----------|
| Primary | Main CTAs — "Find My Vehicle", "Compare" |
| Secondary | Supporting actions — "Save", "Share" |
| Outline | Tertiary actions — "Learn More" |
| Ghost | Inline actions — "View All" |
| Danger | Destructive actions — "Delete Listing" |
| Icon | Icon-only — Favorites heart, close |

### 8.2 Cards

| Type | Use Case |
|------|----------|
| Vehicle Card | Search results, listings, recommendations |
| Listing Card | Marketplace listings with price and trust indicators |
| Brand Card | Brand grid on homepage and browse |
| Article Card | News, guides, reviews |
| Dealer Card | Dealer profiles in search |
| Comparison Card | Side-by-side vehicle comparison |
| Score Card | WANAIA Score display |
| Stat Card | Dashboard statistics |

### 8.3 Data Display

| Component | Use Case |
|-----------|----------|
| Spec Table | Vehicle specifications (key-value pairs) |
| Comparison Table | Side-by-side spec comparison |
| Score Badge | WANAIA Score (circular, color-coded) |
| Deal Badge | Deal Score indicator |
| Trust Indicators | Verification badges |
| Rating Stars | User ratings |
| Price Display | Currency-aware price formatting |
| Stat Bar | Progress bars for ratings breakdown |

### 8.4 Navigation

| Component | Use Case |
|-----------|----------|
| Top Nav | Desktop header with search, auth, language |
| Bottom Nav | Mobile navigation (5 tabs) |
| Breadcrumbs | Hierarchical navigation |
| Tabs | Content sections within a page |
| Sidebar | Filter panel, admin navigation |
| Mega Menu | Brands and categories dropdown |

### 8.5 Feedback

| Component | Use Case |
|-----------|----------|
| Toast | Success/error notifications |
| Loading Skeleton | Content loading states |
| Empty State | No results, first-time states |
| Error State | Error recovery |
| Confirmation Dialog | Destructive action confirmation |
| Progress Bar | Multi-step forms |

---

## 9. Animation System

```scss
// Transitions
$transition-fast:    150ms cubic-bezier(0.4, 0, 0.2, 1);
$transition-base:    200ms cubic-bezier(0.4, 0, 0.2, 1);
$transition-slow:    300ms cubic-bezier(0.4, 0, 0.2, 1);

// Respect reduced motion
@media (prefers-reduced-motion: reduce) {
  * {
    animation-duration: 0.01ms !important;
    transition-duration: 0.01ms !important;
  }
}
```

### Micro-Interactions

| Element | Animation |
|---------|-----------|
| Buttons | Scale on press (0.98), color transition on hover |
| Cards | Shadow elevation on hover, subtle translateY(-2px) |
| Favorites | Heart pulse animation on toggle |
| Score | Number count-up on view |
| Gallery | Smooth slide transitions |
| Search | Filter panel slide in/out |
| Skeleton | Shimmer animation during loading |
| Page transitions | Fade in on route change |

---

## 10. RTL Support

```scss
// Use logical properties for automatic RTL
.card {
  margin-inline-start: $space-4;     // Not margin-left
  padding-inline-end: $space-6;      // Not padding-right
  text-align: start;                 // Not text-align: left
  border-inline-start: 3px solid;    // Not border-left
}

// Direction-aware flexbox
.nav {
  display: flex;
  gap: $space-4;
  // flex-direction inherits from document dir
}

// RTL-specific overrides when needed
[dir="rtl"] {
  .icon-arrow {
    transform: scaleX(-1);
  }
}
```

---

## 11. Dark Mode (Future)

Architecture supports dark mode with CSS custom properties:

```scss
:root {
  --bg-primary: #{$bg-primary};
  --bg-elevated: #{$bg-elevated};
  --text-primary: #{$text-primary};
  --text-secondary: #{$text-secondary};
}

@media (prefers-color-scheme: dark) {
  :root {
    --bg-primary: #{$gray-950};
    --bg-elevated: #{$gray-900};
    --text-primary: #{$gray-50};
    --text-secondary: #{$gray-400};
  }
}
```

---

## 12. Android Design Language

The Android app follows the same WANAIA visual identity while respecting Material Design conventions:

| Element | Web | Android |
|---------|-----|---------|
| Colors | Same palette | Same palette via colors.xml |
| Typography | Inter + Noto Sans Arabic | Same fonts |
| Spacing | Same scale | dp equivalents |
| Cards | Custom elevation | Material CardView |
| Navigation | Custom top + bottom | Bottom Navigation + Toolbar |
| Loading | Custom skeleton | ShimmerLayout |
| Transitions | CSS transitions | Android transitions |

---

## 13. Accessibility

| Requirement | Implementation |
|-------------|---------------|
| Contrast (AA) | All text meets 4.5:1 ratio |
| Focus indicators | Visible focus ring on all interactive elements |
| Keyboard navigation | Full keyboard support |
| Screen readers | Semantic HTML + ARIA labels |
| Touch targets | Minimum 44×44px |
| Alt text | All images have descriptive alt text |
| Form labels | All inputs have associated labels |
| Error messaging | Clear, specific error messages |
| Reduced motion | Respects `prefers-reduced-motion` |

---

*The WANAIA design system creates a premium, trustworthy experience that works beautifully in Arabic, French, and English — on both web and mobile.*
