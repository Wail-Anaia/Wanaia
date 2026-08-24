package com.wanaia.domain.mobility.port.inbound;

import com.wanaia.domain.mobility.dto.MobilityProductDetailDto;
import com.wanaia.domain.mobility.dto.MobilityProductSummaryDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MobilityProductQueryPort {
    Optional<MobilityProductDetailDto> getProductDetailById(Long id);
    Optional<MobilityProductDetailDto> getProductDetailByUuid(UUID uuid);
    List<MobilityProductSummaryDto> getProductsByGenerationId(Long generationId);
    List<MobilityProductSummaryDto> getAllActiveProducts();
    boolean existsById(Long id);
}
