package com.hedgerock.app_server.service.user_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedgerock.app_server.dto.Roles;
import com.hedgerock.app_server.dto.auth.RegisterDTO;
import com.hedgerock.app_server.dto.users.CurrentUserDTO;
import com.hedgerock.app_server.dto.users.UserDTO;
import com.hedgerock.app_server.entity.AuthoritiesEntity;
import com.hedgerock.app_server.entity.UserEntity;
import com.hedgerock.app_server.exceptions.UserNotFoundException;
import com.hedgerock.app_server.repository.AuthorityRepository;
import com.hedgerock.app_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyUserService implements UserService {
    private static final int TIME_TO_LIVE_IN_MINUTES = 10;
    private static final String JSON_PROCESSING_EXCEPTION_MESSAGE = "Failed to cache user {}";

    private final UserRepository repository;
    private final AuthorityRepository authorityRepository;
    private final RedisTemplate<String ,String> redisTemplate;
    private final ObjectMapper objectMapper;

    private String getKeyToken(String token) {
        return "confirm:" + token;
    }

    private void showErrorMessage(Object value) {
        log.error(JSON_PROCESSING_EXCEPTION_MESSAGE, value);
    }

    @Override
    public void cachePendingUser(String token, RegisterDTO registerDTO) {
        try {
            String key = getKeyToken(token);
            String value = objectMapper.writeValueAsString(registerDTO);

            redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(TIME_TO_LIVE_IN_MINUTES));
        } catch (JsonProcessingException e) {
            showErrorMessage(registerDTO);
        }
    }

    @Override
    @Transactional
    public UserEntity confirmUser(String token) {
        String key = getKeyToken(token);
        String cached = redisTemplate.opsForValue().get(key);

        if (cached == null) throw new IllegalArgumentException("Token not found or not valid anymore " + token);

        try {
            RegisterDTO registerDTO = objectMapper.readValue(cached, RegisterDTO.class);
            AuthoritiesEntity userRole = authorityRepository
                    .findByAuthority(Roles.USER.name()).orElseThrow();

            redisTemplate.delete(key);
            return repository.save(registerDTO.toEntity(userRole));
        } catch (JsonProcessingException e) {
            showErrorMessage(cached);
            throw new RuntimeException();
        }
    }

    @Override
    public CurrentUserDTO getById(Long id) {
        final UserEntity user = repository.findById(id).orElseThrow(
                () -> new UserNotFoundException(String.format("User with id %d not found", id))
        );

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
