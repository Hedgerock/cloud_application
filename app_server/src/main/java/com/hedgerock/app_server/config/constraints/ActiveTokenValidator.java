package com.hedgerock.app_server.config.constraints;

import com.hedgerock.app_server.config.constraints.types.TokenType;
import com.hedgerock.app_server.dto.auth.annotations.ValidToken;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.hedgerock.app_server.service.ServiceConstants.CONFIRM_EMAIL_TOKEN_KEY;
import static com.hedgerock.app_server.service.ServiceConstants.RESTORE_PASSWORD_TOKEN_KEY;
import static com.hedgerock.app_server.service.ServiceUtils.getKeyToken;

@Component
@RequiredArgsConstructor
public class ActiveTokenValidator implements ConstraintValidator<ValidToken, String> {
    private final RedisTemplate<String, String> redisTemplate;
    private TokenType tokenType;

    @Override
    public void initialize(ValidToken constraintAnnotation) {
        this.tokenType = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(String token, ConstraintValidatorContext context) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        final String key = getKeyToken(tokenType.getValue(), token);
        return StringUtils.hasText(token) && redisTemplate.opsForValue().get(key) != null;
    }
}
