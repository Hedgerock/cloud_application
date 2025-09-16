package com.hedgerock.app_server.config.constraints;

import com.hedgerock.app_server.config.constraints.types.TokenType;
import com.hedgerock.app_server.dto.auth.annotations.UniqueRequest;
import com.hedgerock.app_server.service.ServiceUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueRequestValidator implements ConstraintValidator<UniqueRequest, String> {

    private final RedisTemplate<String, String> redisTemplate;
    private TokenType tokenType;

    @Override
    public void initialize(UniqueRequest constraintAnnotation) {
        this.tokenType = constraintAnnotation.tokenType();
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        String key = ServiceUtils.getKeyToken(tokenType.getValue(), email);

        return !redisTemplate.hasKey(key);
    }
}
