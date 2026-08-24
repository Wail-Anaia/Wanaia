package com.wanaia.domain.profile.port.inbound;

import com.wanaia.domain.profile.dto.MobilityProfileDto;

import java.util.Optional;

public interface MobilityProfilePort {
    Optional<MobilityProfileDto> getProfileByUserId(Long userId);
    MobilityProfileDto saveOrUpdateProfile(Long userId, MobilityProfileDto dto);
}
