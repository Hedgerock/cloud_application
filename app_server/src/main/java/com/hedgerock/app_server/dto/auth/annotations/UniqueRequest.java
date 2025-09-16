package com.hedgerock.app_server.dto.auth.annotations;

import com.hedgerock.app_server.config.constraints.UniqueRequestValidator;
import com.hedgerock.app_server.config.constraints.types.TokenType;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueRequestValidator.class)
public @interface UniqueRequest {
    String message() default "Message has already sent into this email, try again later";
    TokenType tokenType();

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
