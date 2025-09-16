package com.hedgerock.app_server.dto.auth.validation;

import com.hedgerock.app_server.config.constraints.types.TokenType;
import com.hedgerock.app_server.dto.auth.annotations.ValidToken;
import jakarta.validation.constraints.NotEmpty;

public record ValidationTokenDTO(
        @NotEmpty
        @ValidToken(type = TokenType.CONFIRM_EMAIL)
        String token
) {
}
