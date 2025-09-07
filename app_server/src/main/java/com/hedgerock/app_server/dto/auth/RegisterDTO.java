package com.hedgerock.app_server.dto.auth;

import com.hedgerock.app_server.dto.auth.emailValidation.UniqueEmail;
import com.hedgerock.app_server.entity.AuthoritiesEntity;
import com.hedgerock.app_server.entity.UserEntity;
import jakarta.validation.constraints.*;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Builder
public record RegisterDTO(
        @NotNull
        @NotEmpty
        @NotBlank
        @Email
        @UniqueEmail
        String email,

        @Size(min = 6, max = 20)
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
