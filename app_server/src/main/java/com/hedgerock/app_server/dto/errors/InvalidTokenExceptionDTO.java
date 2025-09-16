package com.hedgerock.app_server.dto.errors;

import com.hedgerock.app_server.exceptions.InvalidTokenException;
import lombok.Builder;

@Builder
public record InvalidTokenExceptionDTO(
        String message,
        int code
) {

    public static InvalidTokenExceptionDTO toDTO(InvalidTokenException e, int statusCode) {
        return InvalidTokenExceptionDTO.builder()
                .message(e.getMessage())
                .code(statusCode)
                .build();
    }
}
