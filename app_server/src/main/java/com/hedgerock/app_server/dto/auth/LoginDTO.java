package com.hedgerock.app_server.dto.auth;

import com.hedgerock.app_server.dto.auth.annotations.EmailExists;
import com.hedgerock.app_server.entity.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import static com.hedgerock.app_server.dto.ValidationConstants.MAX_PASSWORD_LENGTH;
import static com.hedgerock.app_server.dto.ValidationConstants.MIN_PASSWORD_LENGTH;

@Builder
public record LoginDTO(
        @Email
        @EmailExists
        String email,
       @Size(min = MIN_PASSWORD_LENGTH, max = MAX_PASSWORD_LENGTH)
       String password
) {

    public static LoginDTO fromEntity(UserEntity user) {
        return LoginDTO.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

}
