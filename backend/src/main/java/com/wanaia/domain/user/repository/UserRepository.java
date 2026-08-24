package com.wanaia.domain.user.repository;

import com.wanaia.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndDeletedAtIsNull(String email);
    Optional<User> findByUuidAndDeletedAtIsNull(UUID uuid);
    boolean existsByEmailAndDeletedAtIsNull(String email);
}
