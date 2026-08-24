package com.wanaia.domain.decision.model;

import com.wanaia.common.base.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "score_explanation_items", indexes = {
    @Index(name = "idx_explanations_score_result", columnList = "score_result_id")
})
public class ScoreExplanationItem extends BaseEntity {

    @Column(name = "score_result_id", nullable = false)
    private Long scoreResultId; // Reference to ScoreResult

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private ExplanationType type; // PRO, CON, WARNING

    @Column(name = "category", nullable = false, length = 50)
    private String category; // "EFFICIENCY", "RELIABILITY", "BUDGET", "ERGONOMICS"

    @Column(name = "code", nullable = false, length = 80)
    private String code; // e.g. "LOW_ANNUAL_MAINTENANCE_COST"

    @Column(name = "message_template", nullable = false, length = 500)
    private String messageTemplate;

    @Column(name = "parameters_json", columnDefinition = "TEXT")
    private String parametersJson;

    @Column(name = "provenance_ref", length = 255)
    private String provenanceRef;

    public ScoreExplanationItem() {}

    public ScoreExplanationItem(Long scoreResultId, ExplanationType type, String category, String code,
                                String messageTemplate, String parametersJson, String provenanceRef) {
        this.scoreResultId = scoreResultId;
        this.type = type;
        this.category = category;
        this.code = code;
        this.messageTemplate = messageTemplate;
        this.parametersJson = parametersJson;
        this.provenanceRef = provenanceRef;
    }

    public Long getScoreResultId() {
        return scoreResultId;
    }

    public void setScoreResultId(Long scoreResultId) {
        this.scoreResultId = scoreResultId;
    }

    public ExplanationType getType() {
        return type;
    }

    public void setType(ExplanationType type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }

    public void setMessageTemplate(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public String getParametersJson() {
        return parametersJson;
    }

    public void setParametersJson(String parametersJson) {
        this.parametersJson = parametersJson;
    }

    public String getProvenanceRef() {
        return provenanceRef;
    }

    public void setProvenanceRef(String provenanceRef) {
        this.provenanceRef = provenanceRef;
    }
}
