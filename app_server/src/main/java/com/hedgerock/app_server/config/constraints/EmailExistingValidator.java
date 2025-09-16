package com.hedgerock.app_server.config.constraints;

import com.hedgerock.app_server.dto.auth.annotations.EmailExists;
import com.hedgerock.app_server.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class EmailExistingValidator implements ConstraintValidator<EmailExists, String> {
    private final UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        final boolean isExists = StringUtils.hasText(email) && userRepository.existsByEmail(email);

        if (!isExists) {
            context.disableDefaultConstraintViolation();
            context
                .buildConstraintViolationWithTemplate(String.format("Email %s doesn't exist", email))
                .addConstraintViolation();
        }

        return isExists;
    }
}
