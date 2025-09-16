package com.hedgerock.app_server.dto.auth;

import com.hedgerock.app_server.config.constraints.types.TokenType;
import com.hedgerock.app_server.dto.auth.annotations.EmailExists;
import com.hedgerock.app_server.dto.auth.annotations.UniqueRequest;
import jakarta.validation.constraints.Email;
import lombok.Builder;

@Builder
public record RestorePasswordDTO(
        @Email
        @EmailExists
        @UniqueRequest(tokenType = TokenType.RESTORE_PASSWORD)
        String email
) {
}
