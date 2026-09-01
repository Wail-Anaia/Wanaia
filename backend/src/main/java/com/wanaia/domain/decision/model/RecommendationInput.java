package com.wanaia.domain.decision.model;

import com.wanaia.domain.mobility.dto.MobilityProductDetailDto;
import com.wanaia.domain.profile.model.MobilityProfile;

import java.util.List;

public record RecommendationInput(
    Long userId,
    MobilityProfile userProfile,
    MarketContext marketContext,
    List<MobilityProductDetailDto> candidateProducts
) {}
