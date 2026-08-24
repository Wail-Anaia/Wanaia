package com.wanaia.domain.ai.port.inbound;

import java.util.Map;

public interface AiAdvisorPort {
    String generateGroundedResponse(String userPrompt, Map<String, Object> groundedContext);
}
