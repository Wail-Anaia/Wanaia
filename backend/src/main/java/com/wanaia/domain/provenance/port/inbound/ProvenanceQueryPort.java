package com.wanaia.domain.provenance.port.inbound;

import com.wanaia.domain.provenance.dto.AttributeProvenanceDto;
import com.wanaia.domain.provenance.dto.DataSourceDto;

import java.util.List;
import java.util.Optional;

public interface ProvenanceQueryPort {
    List<AttributeProvenanceDto> getProvenanceForEntity(String entityType, Long entityId);
    List<AttributeProvenanceDto> getProvenanceForEntityInMarket(String entityType, Long entityId, String marketCode);
    Optional<DataSourceDto> getDataSourceByCode(String code);
    List<DataSourceDto> getAllDataSources();
}
