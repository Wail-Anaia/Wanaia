package com.wanaia.domain.mobility.repository;

import com.wanaia.domain.mobility.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
    Optional<Model> findByBrandIdAndSlug(Long brandId, String slug);
    List<Model> findByBrandIdAndIsActiveTrue(Long brandId);
}
