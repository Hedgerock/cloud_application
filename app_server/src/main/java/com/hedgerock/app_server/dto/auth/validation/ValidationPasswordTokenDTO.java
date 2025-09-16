package com.hedgerock.app_server.dto.auth.validation;

import com.hedgerock.app_server.config.constraints.types.TokenType;
import com.hedgerock.app_server.dto.auth.annotations.NotTheSamePassword;
import com.hedgerock.app_server.dto.auth.annotations.ValidToken;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import static com.hedgerock.app_server.dto.ValidationConstants.MAX_PASSWORD_LENGTH;
import static com.hedgerock.app_server.dto.ValidationConstants.MIN_PASSWORD_LENGTH;

@Builder
@NotTheSamePassword
public record ValidationPasswordTokenDTO(
        @ValidToken(type = TokenType.RESTORE_PASSWORD)
        @NotEmpty
        String token,
        @Size(max = MAX_PASSWORD_LENGTH, min = MIN_PASSWORD_LENGTH)
        String password
) {
}
