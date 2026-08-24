package com.wanaia.domain.provenance.controller;

import com.wanaia.common.api.ApiResponse;
import com.wanaia.domain.provenance.dto.AttributeProvenanceDto;
import com.wanaia.domain.provenance.dto.DataSourceDto;
import com.wanaia.domain.provenance.port.inbound.ProvenanceQueryPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/provenance")
@Tag(name = "Data Provenance & Trust", description = "Endpoints for verifying data sources, epistemic classifications, and attribute verification status.")
public class ProvenanceController {

    private final ProvenanceQueryPort provenanceQueryPort;

    public ProvenanceController(ProvenanceQueryPort provenanceQueryPort) {
        this.provenanceQueryPort = provenanceQueryPort;
    }

    @GetMapping("/sources")
    @Operation(summary = "List all registered data sources and authority tiers")
    public ResponseEntity<ApiResponse<List<DataSourceDto>>> getAllDataSources() {
        return ResponseEntity.ok(ApiResponse.success(provenanceQueryPort.getAllDataSources()));
    }

    @GetMapping("/entities/{entityType}/{entityId}")
    @Operation(summary = "Get attribute provenance and epistemic classifications for an entity")
    public ResponseEntity<ApiResponse<List<AttributeProvenanceDto>>> getProvenanceForEntity(
        @PathVariable String entityType,
        @PathVariable Long entityId,
        @RequestParam(required = false) String marketCode
    ) {
        List<AttributeProvenanceDto> list = (marketCode != null && !marketCode.trim().isEmpty())
            ? provenanceQueryPort.getProvenanceForEntityInMarket(entityType, entityId, marketCode)
            : provenanceQueryPort.getProvenanceForEntity(entityType, entityId);
        return ResponseEntity.ok(ApiResponse.success(list));
    }
}
