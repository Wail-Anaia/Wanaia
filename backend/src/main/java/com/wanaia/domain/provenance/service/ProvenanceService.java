package com.wanaia.domain.provenance.service;

import com.wanaia.domain.provenance.dto.AttributeProvenanceDto;
import com.wanaia.domain.provenance.dto.DataSourceDto;
import com.wanaia.domain.provenance.model.AttributeProvenance;
import com.wanaia.domain.provenance.model.DataSource;
import com.wanaia.domain.provenance.port.inbound.ProvenanceQueryPort;
import com.wanaia.domain.provenance.repository.AttributeProvenanceRepository;
import com.wanaia.domain.provenance.repository.DataSourceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProvenanceService implements ProvenanceQueryPort {

    private final DataSourceRepository dataSourceRepository;
    private final AttributeProvenanceRepository attributeProvenanceRepository;

    public ProvenanceService(DataSourceRepository dataSourceRepository,
                             AttributeProvenanceRepository attributeProvenanceRepository) {
        this.dataSourceRepository = dataSourceRepository;
        this.attributeProvenanceRepository = attributeProvenanceRepository;
    }

    @Override
    public List<AttributeProvenanceDto> getProvenanceForEntity(String entityType, Long entityId) {
        List<AttributeProvenance> records = attributeProvenanceRepository.findByEntityTypeAndEntityId(entityType, entityId);
        Map<Long, String> sourceNames = getSourceNamesMap();
        return records.stream()
            .map(r -> mapToDto(r, sourceNames.get(r.getSourceId())))
            .collect(Collectors.toList());
    }

    @Override
    public List<AttributeProvenanceDto> getProvenanceForEntityInMarket(String entityType, Long entityId, String marketCode) {
        List<AttributeProvenance> records = attributeProvenanceRepository.findByEntityTypeAndEntityIdAndMarketCode(entityType, entityId, marketCode);
        Map<Long, String> sourceNames = getSourceNamesMap();
        return records.stream()
            .map(r -> mapToDto(r, sourceNames.get(r.getSourceId())))
            .collect(Collectors.toList());
    }

    @Override
    public Optional<DataSourceDto> getDataSourceByCode(String code) {
        return dataSourceRepository.findByCode(code).map(this::mapSourceToDto);
    }

    @Override
    public List<DataSourceDto> getAllDataSources() {
        return dataSourceRepository.findAll().stream()
            .map(this::mapSourceToDto)
            .collect(Collectors.toList());
    }

    private Map<Long, String> getSourceNamesMap() {
        return dataSourceRepository.findAll().stream()
            .collect(Collectors.toMap(DataSource::getId, DataSource::getName, (k1, k2) -> k1));
    }

    private AttributeProvenanceDto mapToDto(AttributeProvenance r, String sourceName) {
        return new AttributeProvenanceDto(
            r.getId(),
            r.getEntityType(),
            r.getEntityId(),
            r.getAttributeName(),
            r.getEpistemicType().name(),
            r.getSourceId(),
            sourceName != null ? sourceName : "Official Authority",
            r.getSourceReference(),
            r.getCollectedAt(),
            r.getValidFrom(),
            r.getValidTo(),
            r.getMarketCode(),
            r.getVerificationStatus().name(),
            r.getConfidenceLevel().name()
        );
    }

    private DataSourceDto mapSourceToDto(DataSource s) {
        return new DataSourceDto(
            s.getId(),
            s.getCode(),
            s.getName(),
            s.getSourceType(),
            s.getTrustTier(),
            s.getWebsiteUrl()
        );
    }
}
