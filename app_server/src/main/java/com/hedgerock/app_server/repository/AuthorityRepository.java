package com.hedgerock.app_server.repository;

import com.hedgerock.app_server.entity.AuthoritiesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<AuthoritiesEntity, Long> {

    Optional<AuthoritiesEntity> findByAuthority(String authority);

}
