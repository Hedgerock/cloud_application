package com.hedgerock.app_server.dto.errors;

import com.hedgerock.app_server.exceptions.CurrentBindException;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record BindExceptionDTO(String message, List<String> errors, int code, Instant now) {

    public static BindExceptionDTO toDTO(CurrentBindException e) {
        return BindExceptionDTO.builder()
                .message(e.getMessage())
                .errors(e.getErrors())
                .code(400)
                .now(Instant.now())
                .build();
    }
}
