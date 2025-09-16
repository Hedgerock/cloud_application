package com.hedgerock.app_server.dto.auth;

import com.hedgerock.app_server.config.constraints.types.TokenType;
import com.hedgerock.app_server.dto.auth.annotations.UniqueEmail;
import com.hedgerock.app_server.dto.auth.annotations.UniqueRequest;
import com.hedgerock.app_server.entity.AuthoritiesEntity;
import com.hedgerock.app_server.entity.UserEntity;
import jakarta.validation.constraints.*;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static com.hedgerock.app_server.dto.ValidationConstants.MAX_PASSWORD_LENGTH;
import static com.hedgerock.app_server.dto.ValidationConstants.MIN_PASSWORD_LENGTH;

@Builder
public record RegisterDTO(
        @NotNull
        @NotEmpty
        @NotBlank
        @Email
        @UniqueEmail
        @UniqueRequest(tokenType = TokenType.CONFIRM_EMAIL)
        String email,

        @Size(min = MIN_PASSWORD_LENGTH, max = MAX_PASSWORD_LENGTH)
        String password
) {
    public RegisterDTO getEncryptedDTO(PasswordEncoder passwordEncoder) {
        return RegisterDTO.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
    }

    public UserEntity toEntity(AuthoritiesEntity userRole) {

        return UserEntity.builder()
                .email(email)
                .password(password)
                .diskSpace(10737418240L)
                .usedSpace(0L)
                .authorities(List.of(userRole))
                .build();
    }

}
