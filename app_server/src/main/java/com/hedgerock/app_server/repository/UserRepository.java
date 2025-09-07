package com.hedgerock.app_server.repository;

import com.hedgerock.app_server.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
