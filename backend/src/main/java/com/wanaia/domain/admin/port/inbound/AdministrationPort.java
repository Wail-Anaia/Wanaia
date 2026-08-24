package com.wanaia.domain.admin.port.inbound;

import java.util.Map;

public interface AdministrationPort {
    Map<String, Object> getSystemHealthOverview();
}
