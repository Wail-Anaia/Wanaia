# WANAIA — Native Android Architecture

## Native Android Architecture (Java)

---

## 1. Domain Models & Network Layer

All Android data models are aligned with the OpenAPI contract:

```java
// data/model/MobilityProduct.java
public class MobilityProduct {
    private long id;
    private String uuid;
    private String brandName;
    private String modelName;
    private String generationName;
    private String trimName;
    private String slug;
    private int modelYear;
    private String category; // "CAR", "MOTORCYCLE", "VAN"
    private String bodyType;
    
    private PowertrainDto powertrain;
    private MarketContextDto marketContext;
    private DecisionScoresDto scores;
    
    // Getters and Setters
}

// data/model/DecisionScoresDto.java
public class DecisionScoresDto {
    private BigDecimal globalWanaiaScore;
    private BigDecimal personalFitScore;
    private BigDecimal dealScore;
    private String dealClassification;
    private List<ScoreExplanationDto> explanations;
    
    // Getters and Setters
}
```

---

## 2. Shared API Consumption & Repository Architecture

```java
public class MobilityRepository {
    private final WanaiaApiService api;
    private final MobilityProductDao productDao;
    
    public LiveData<Resource<MobilityProduct>> getProductMaster(long productId, String marketCode) {
        MutableLiveData<Resource<MobilityProduct>> data = new MutableLiveData<>(Resource.loading());
        
        api.getProductMaster(productId, marketCode).enqueue(new Callback<ApiResponse<MobilityProduct>>() {
            @Override
            public void onResponse(Call<ApiResponse<MobilityProduct>> call, Response<ApiResponse<MobilityProduct>> res) {
                if (res.isSuccessful() && res.body() != null && res.body().isSuccess()) {
                    data.postValue(Resource.success(res.body().getData()));
                } else {
                    data.postValue(Resource.error("Failed to load mobility product"));
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<MobilityProduct>> call, Throwable t) {
                data.postValue(Resource.error(t.getMessage()));
            }
        });
        
        return data;
    }
}
```

---

## 3. UI Consistency & The WANAIA Mobile Design Language

The Android UI uses custom Compound Views to mirror the Web Design System:
- `ScoreBadgeView`: Custom canvas-rendered circular progress badge.
- `FitScoreComparisonView`: Side-by-side bar comparing Global Score vs Personal Fit.
- `DealBadgeView`: Styled Chip reflecting Deal Score status.
- Full RTL layout mirroring for Arabic locale (`res/values-ar/strings.xml`, `layoutDirection="locale"`).

---

*This architecture guarantees strict API parity with the web application and flawless native performance on Android devices.*
