package com.wanaia.domain.mobility.repository;

import com.wanaia.domain.mobility.model.Transmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransmissionRepository extends JpaRepository<Transmission, Long> {
    Optional<Transmission> findByName(String name);
}
