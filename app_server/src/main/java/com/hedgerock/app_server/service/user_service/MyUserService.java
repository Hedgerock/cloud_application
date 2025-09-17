package com.hedgerock.app_server.service.user_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedgerock.app_server.config.constraints.types.TokenType;
import com.hedgerock.app_server.dto.Roles;
import com.hedgerock.app_server.dto.auth.RegisterDTO;
import com.hedgerock.app_server.dto.auth.validation.ValidationPasswordTokenDTO;
import com.hedgerock.app_server.dto.users.CurrentUserDTO;
import com.hedgerock.app_server.dto.users.UserDTO;
import com.hedgerock.app_server.entity.AuthoritiesEntity;
import com.hedgerock.app_server.entity.UserEntity;
import com.hedgerock.app_server.exceptions.*;
import com.hedgerock.app_server.repository.AuthorityRepository;
import com.hedgerock.app_server.repository.UserRepository;
import com.hedgerock.app_server.service.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

import static com.hedgerock.app_server.service.ServiceConstants.*;
import static com.hedgerock.app_server.service.ServiceUtils.getKeyToken;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyUserService implements UserService {
    private static final String JSON_PROCESSING_EXCEPTION_MESSAGE = "Failed to cache user {}";
    private static final String INVALID_TOKEN_EXCEPTION_MESSAGE = "Token not found or not valid anymore";

    private final UserRepository repository;
    private final AuthorityRepository authorityRepository;
    private final RedisTemplate<String ,String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;

    private void showErrorMessage(Object value) {
        log.error(JSON_PROCESSING_EXCEPTION_MESSAGE, value);
    }

    @Override
    public void cachePendingUser(TokenType tokenType, String token, RegisterDTO registerDTO) {
        try {
            String key = getKeyToken(tokenType.getValue(), token);
            String emailKey = getKeyToken(TokenType.CONFIRM_EMAIL.getValue(), registerDTO.email());
            String value = objectMapper.writeValueAsString(registerDTO);

            redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(TIME_TO_LIVE_IN_MINUTES));
            redisTemplate.opsForValue().set(emailKey, token, Duration.ofMinutes(TIME_TO_LIVE_IN_MINUTES));
        } catch (JsonProcessingException e) {
            showErrorMessage(registerDTO);
            throw new CacheValueException(String.format("Failed to cache user %s", registerDTO.email()));
        }
    }

    @Override
    public void cachePendingEmailForRestore(TokenType tokenType, String token, String email) {
        String key = getKeyToken(tokenType.getValue(), token);
        String emailKey = getKeyToken(tokenType.getValue(), email);

        redisTemplate.opsForValue().set(key, email, Duration.ofMinutes(TIME_TO_LIVE_IN_MINUTES));
        redisTemplate.opsForValue().set(emailKey, token, Duration.ofMinutes(TIME_TO_LIVE_IN_MINUTES));
    }

    @Override
    @Transactional
    public void confirmUser(String token) {
        String key = getKeyToken(CONFIRM_EMAIL_TOKEN_KEY, token);
        String cached = redisTemplate.opsForValue().get(key);

        if (cached == null) {
            throw new InvalidTokenException(INVALID_TOKEN_EXCEPTION_MESSAGE);
        }

        try {
            RegisterDTO registerDTO = objectMapper.readValue(cached, RegisterDTO.class);
            String emailKey = getKeyToken(CONFIRM_EMAIL_TOKEN_KEY, registerDTO.email());
            AuthoritiesEntity userRole = authorityRepository
                .findByAuthority(Roles.USER.name())
                .orElseThrow(() ->
                    new RoleNotFoundException(String.format("Role %s not found", Roles.USER.name())
                )
            );

            ServiceUtils.deleteRedisKeys(redisTemplate, key, emailKey);
            repository.save(registerDTO.toEntity(userRole));

        } catch (JsonProcessingException e) {
            showErrorMessage(cached);
            throw new UserConfirmException("Failed to confirm user");
        }
    }

    @Override
    @Transactional
    public void confirmPassword(ValidationPasswordTokenDTO validationPasswordTokenDTO) {
        String key = getKeyToken(RESTORE_PASSWORD_TOKEN_KEY, validationPasswordTokenDTO.token());
        String email = redisTemplate.opsForValue().get(key);

        if (email == null) {
            throw new InvalidTokenException(INVALID_TOKEN_EXCEPTION_MESSAGE);
        }

        String emailKey = getKeyToken(RESTORE_PASSWORD_TOKEN_KEY, email);

        UserEntity user = repository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException(String.format("User with email %s not found", email)));

        user.setPassword(passwordEncoder.encode(validationPasswordTokenDTO.password()));

        ServiceUtils.deleteRedisKeys(redisTemplate, key, emailKey);
        repository.save(user);
    }

    @Override
    public CurrentUserDTO getById(Long id) {
        final UserEntity user = repository.findById(id)
            .orElseThrow(() ->
                new UserNotFoundException(String.format("User with id %d not found", id)
            )
        );

        final CurrentUserDTO userDTO = CurrentUserDTO.toDTO(user);
        log.info("Get user {}", userDTO);

        return userDTO;
    }

    @Override
    public List<UserDTO> getAll() {
        final List<UserEntity> users = repository.findAll();

        final List<UserDTO> userDTOS = users.stream().map(UserDTO::toDTO).toList();
        log.info("Get list {}", userDTOS);

        return userDTOS;
    }
}
