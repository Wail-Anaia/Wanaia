package com.wanaia.domain.market.repository;

import com.wanaia.domain.market.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByIsoCode2(String isoCode2);
    Optional<Country> findByIsoCode3(String isoCode3);
}
