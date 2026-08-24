# WANAIA — Search Architecture

## Smart Search & Discovery

---

## 1. Search Vision

WANAIA search goes beyond keyword matching. Users should be able to express what they need in natural language, and the system should understand intent, apply structured filters, and return ranked results.

---

## 2. Search Architecture Layers

```
┌─────────────────────────────────────────────────┐
│              Search Entry Points                 │
│                                                  │
│  Hero Search Bar │ Filter Panel │ NLP Query       │
│  Autocomplete    │ Facets       │ AI-assisted     │
└────────┬─────────┴──────┬───────┴────────────────┘
         │                │
┌────────▼────────────────▼────────────────────────┐
│            Search Service (Interface)             │
│                                                  │
│  searchVehicles(SearchRequest) → SearchResponse  │
│  suggest(query) → List<Suggestion>               │
│  getFacets(SearchRequest) → Facets               │
└────────┬────────────────┬────────────────────────┘
         │                │
    ┌────▼────┐    ┌──────▼──────────┐
    │ MVP     │    │  Future         │
    │ Postgres│    │  OpenSearch/    │
    │ FTS     │    │  Elasticsearch  │
    └─────────┘    └─────────────────┘
```

---

## 3. Search Request Model

```java
public class SearchRequest {
    // Text
    private String query;                    // Free text or NLP

    // Vehicle filters
    private List<String> brandSlugs;
    private List<String> modelSlugs;
    private List<String> bodyTypes;
    private List<String> fuelTypes;
    private Integer yearMin;
    private Integer yearMax;
    private Integer mileageMax;

    // Pricing
    private BigDecimal priceMin;
    private BigDecimal priceMax;
    private String currency;

    // Features & performance
    private List<String> features;
    private Integer powerMin;
    private Integer doorsMin;
    private Integer seatsMin;

    // Location
    private String city;
    private String region;
    private String country;
    private Double latitude;
    private Double longitude;
    private Integer radiusKm;

    // Listing type
    private String condition;                // NEW, USED, CERTIFIED_PREOWNED
    private String sellerType;               // PRIVATE, DEALER

    // Pagination & sorting
    private int page = 0;
    private int size = 20;
    private String sortBy = "relevance";     // relevance, price_asc, price_desc, date, mileage
    private String sortDirection = "asc";
}
```

---

## 4. MVP: PostgreSQL Full-Text Search

### 4.1 Search Vector

```sql
-- Maintain tsvector column on listings
CREATE OR REPLACE FUNCTION update_listing_search_vector()
RETURNS TRIGGER AS $$
BEGIN
    NEW.search_vector :=
        setweight(to_tsvector('simple', COALESCE(NEW.brand_name, '')), 'A') ||
        setweight(to_tsvector('simple', COALESCE(NEW.model_name, '')), 'A') ||
        setweight(to_tsvector('simple', COALESCE(NEW.title, '')), 'B') ||
        setweight(to_tsvector('simple', COALESCE(NEW.description, '')), 'D') ||
        setweight(to_tsvector('simple', COALESCE(NEW.city, '')), 'C');
    RETURN NEW;
END
$$ LANGUAGE plpgsql;

CREATE TRIGGER listing_search_vector_trigger
    BEFORE INSERT OR UPDATE ON listings
    FOR EACH ROW EXECUTE FUNCTION update_listing_search_vector();
```

### 4.2 JPA Specification Builder

```java
@Component
public class ListingSpecificationBuilder {

    public Specification<Listing> buildFrom(SearchRequest request) {
        Specification<Listing> spec = Specification.where(isActive());

        if (hasText(request.getQuery())) {
            spec = spec.and(matchesFullText(request.getQuery()));
        }
        if (isNotEmpty(request.getBrandSlugs())) {
            spec = spec.and(brandsIn(request.getBrandSlugs()));
        }
        if (request.getPriceMin() != null) {
            spec = spec.and(priceGreaterThan(request.getPriceMin()));
        }
        if (request.getPriceMax() != null) {
            spec = spec.and(priceLessThan(request.getPriceMax()));
        }
        // ... all other filters

        return spec;
    }

    private Specification<Listing> matchesFullText(String query) {
        return (root, cq, cb) -> cb.isTrue(
            cb.function("fts_match", Boolean.class,
                root.get("searchVector"),
                cb.literal(query))
        );
    }
}
```

### 4.3 Trigram Similarity (Fuzzy Search)

```sql
-- Enable pg_trgm for typo tolerance
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- Create trigram index on brand and model names
CREATE INDEX idx_brands_name_trgm ON brands USING GIN (name gin_trgm_ops);
CREATE INDEX idx_models_name_trgm ON models USING GIN (name gin_trgm_ops);

-- Fuzzy matching query
SELECT name, similarity(name, 'toyotta') AS sim
FROM brands
WHERE name % 'toyotta'
ORDER BY sim DESC
LIMIT 5;
```

---

## 5. Autocomplete / Suggestions

```java
public interface SuggestionService {
    List<Suggestion> suggest(String query, int limit);
}

public record Suggestion(
    String text,
    String type,       // BRAND, MODEL, BODY_TYPE, QUERY
    String slug,
    String url
) {}

// MVP: Query brands + models + body types with trigram similarity
// Future: Dedicated suggestion index with popularity weighting
```

---

## 6. Faceted Search

```java
public record SearchFacets(
    List<FacetBucket> brands,
    List<FacetBucket> bodyTypes,
    List<FacetBucket> fuelTypes,
    List<FacetBucket> transmissions,
    List<FacetBucket> years,
    List<FacetBucket> cities,
    PriceRange priceRange,
    MileageRange mileageRange
) {}

public record FacetBucket(
    String key,
    String label,
    long count
) {}

// Facets are computed from the current filtered result set
// MVP: PostgreSQL GROUP BY queries
// Future: OpenSearch aggregations
```

---

## 7. Natural Language Search (Phase 2)

```
User input: "reliable family SUV under 300,000 MAD with low fuel consumption"

NLP Processing:
  → body_type: SUV
  → use_case: family (seats >= 5, cargo space)
  → budget_max: 300000
  → currency: MAD
  → priority: reliability
  → efficiency: high (fuel_consumption_l100km < 7)

Structured SearchRequest generated → sent to search service
```

### Integration with AI Module

```java
@Service
public class NlpSearchService {
    private final AiProvider aiProvider;
    private final SearchService searchService;

    public SearchResponse naturalLanguageSearch(String query) {
        // 1. Use AI to extract structured filters from natural language
        SearchRequest structuredRequest = aiProvider.extractSearchCriteria(query);

        // 2. Execute structured search
        return searchService.searchListings(structuredRequest);
    }
}
```

---

## 8. Future: OpenSearch/Elasticsearch Migration

### Boundary Definition

```java
// The SearchService interface is the migration boundary
// Swap PostgresSearchService → ElasticsearchSearchService
// No changes to controllers, clients, or other modules

public interface SearchService {
    PagedResponse<ListingSearchResult> searchListings(SearchRequest request);
    List<Suggestion> suggest(String query, int limit);
    SearchFacets getFacets(SearchRequest request);
}

@Service
@Profile("elasticsearch")
public class ElasticsearchSearchService implements SearchService {
    private final RestHighLevelClient esClient;
    // Full OpenSearch implementation with:
    // - Fuzzy matching
    // - Synonyms
    // - Boosted fields
    // - Geo-distance
    // - Aggregations for facets
    // - Personalized ranking
}
```

### Index Design (Future)

```json
{
  "mappings": {
    "properties": {
      "title": { "type": "text", "analyzer": "wanaia_analyzer" },
      "brand_name": { "type": "keyword", "copy_to": "all_text" },
      "model_name": { "type": "keyword", "copy_to": "all_text" },
      "body_type": { "type": "keyword" },
      "fuel_type": { "type": "keyword" },
      "price": { "type": "float" },
      "year": { "type": "integer" },
      "mileage_km": { "type": "integer" },
      "location": { "type": "geo_point" },
      "city": { "type": "keyword" },
      "features": { "type": "keyword" },
      "all_text": { "type": "text", "analyzer": "wanaia_multilingual" }
    }
  }
}
```

---

## 9. Search Ranking (Future)

| Factor | Weight | Description |
|--------|--------|-------------|
| Text relevance | 30% | Full-text match quality |
| Freshness | 15% | Newer listings ranked higher |
| Completeness | 10% | Listings with more photos/details |
| Seller quality | 10% | Verified sellers, dealer rating |
| Price competitiveness | 10% | Better deals ranked higher |
| Location proximity | 10% | Closer to user |
| Personalization | 15% | User preference match |

---

*The search architecture starts simple with PostgreSQL FTS and trigram similarity, with a clear interface boundary for future OpenSearch/Elasticsearch migration.*
