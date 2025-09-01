package com.hedgerock.app_server.service.user_service;

import com.hedgerock.app_server.dto.CurrentUserDTO;
import com.hedgerock.app_server.dto.UserDTO;
import com.hedgerock.app_server.entity.UserEntity;
import com.hedgerock.app_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyUserService implements UserService {

    private final UserRepository repository;

    @Override
    public CurrentUserDTO getById(Long id) {
        final UserEntity user = repository.findById(id).orElseThrow();

        log.info("Get user {}", user);

        return CurrentUserDTO.toDTO(user);
    }

    @Override
    public List<UserDTO> getAll() {
        final List<UserEntity> users = repository.findAll();

        log.info("Get list {}", users);

        return users.stream().map(UserDTO::toDTO).toList();
    }
}
