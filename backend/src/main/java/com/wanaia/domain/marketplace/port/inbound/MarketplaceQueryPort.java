package com.wanaia.domain.marketplace.port.inbound;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MarketplaceQueryPort {
    long countActiveListings();
    boolean existsListingByUuid(UUID uuid);
}
