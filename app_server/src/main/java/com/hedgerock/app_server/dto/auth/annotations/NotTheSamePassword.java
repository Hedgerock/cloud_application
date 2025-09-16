package com.hedgerock.app_server.dto.auth.annotations;

import com.hedgerock.app_server.config.constraints.NotTheSamePasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Constraint(validatedBy = NotTheSamePasswordValidator.class)
public @interface NotTheSamePassword {
    String message() default "Password mustn't be equals previous";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
