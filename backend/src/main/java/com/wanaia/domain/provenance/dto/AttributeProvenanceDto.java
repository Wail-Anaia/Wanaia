package com.wanaia.domain.provenance.dto;

import java.time.LocalDate;
import java.time.Instant;

public record AttributeProvenanceDto(
    Long id,
    String entityType,
    Long entityId,
    String attributeName,
    String epistemicType,
    Long sourceId,
    String sourceName,
    String sourceReference,
    Instant collectedAt,
    LocalDate validFrom,
    LocalDate validTo,
    String marketCode,
    String verificationStatus,
    String confidenceLevel
) {}
