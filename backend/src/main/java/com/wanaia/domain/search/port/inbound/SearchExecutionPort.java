package com.wanaia.domain.search.port.inbound;

import java.util.List;
import java.util.Map;

public interface SearchExecutionPort {
    List<String> getSuggestions(String query, int limit);
}
