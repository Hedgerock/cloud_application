package com.hedgerock.app_server.config.constraints;

import com.hedgerock.app_server.dto.auth.annotations.NotTheSamePassword;
import com.hedgerock.app_server.dto.auth.validation.ValidationPasswordTokenDTO;
import com.hedgerock.app_server.entity.UserEntity;
import com.hedgerock.app_server.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.hedgerock.app_server.service.ServiceConstants.RESTORE_PASSWORD_TOKEN_KEY;
import static com.hedgerock.app_server.service.ServiceUtils.getKeyToken;

@Component
@RequiredArgsConstructor
public class NotTheSamePasswordValidator implements ConstraintValidator<NotTheSamePassword, ValidationPasswordTokenDTO> {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean isValid(ValidationPasswordTokenDTO value, ConstraintValidatorContext context) {
        String email = redisTemplate.opsForValue().get(getKeyToken(RESTORE_PASSWORD_TOKEN_KEY, value.token()));
        UserEntity user = userRepository.findByEmail(email).orElse(null);
        return user != null && !passwordEncoder.matches(value.password(), user.getPassword());
    }
}
