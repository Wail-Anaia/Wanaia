package com.wanaia.domain.decision.model;

import com.wanaia.common.base.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "score_input_snapshots", indexes = {
    @Index(name = "idx_snapshot_hash", columnList = "snapshot_hash")
})
public class ScoreInputSnapshot extends BaseEntity {

    @Column(name = "snapshot_hash", nullable = false, length = 64)
    private String snapshotHash; // SHA-256 hash of raw input payload

    @Column(name = "raw_payload_json", nullable = false, columnDefinition = "TEXT")
    private String rawPayloadJson; // Complete immutable JSON snapshot of all inputs

    public ScoreInputSnapshot() {}

    public ScoreInputSnapshot(String snapshotHash, String rawPayloadJson) {
        this.snapshotHash = snapshotHash;
        this.rawPayloadJson = rawPayloadJson;
    }

    public String getSnapshotHash() {
        return snapshotHash;
    }

    public void setSnapshotHash(String snapshotHash) {
        this.snapshotHash = snapshotHash;
    }

    public String getRawPayloadJson() {
        return rawPayloadJson;
    }

    public void setRawPayloadJson(String rawPayloadJson) {
        this.rawPayloadJson = rawPayloadJson;
    }
}
