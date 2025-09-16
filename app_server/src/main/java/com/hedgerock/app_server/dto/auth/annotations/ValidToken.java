package com.hedgerock.app_server.dto.auth.annotations;

import com.hedgerock.app_server.config.constraints.ActiveTokenValidator;
import com.hedgerock.app_server.config.constraints.types.TokenType;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ActiveTokenValidator.class)
public @interface ValidToken {
    String message() default "Token not valid";
    TokenType type();

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

