package com.hedgerock.app_server.dto.errors;

import lombok.Builder;

import java.time.Instant;

@Builder
public record CustomizedErrorDTO(
        String message,
        int code,
        Instant timestamp
) {
    public static CustomizedErrorDTO getDefaultResponse() {
        return getDefaultResponse("Credentials not provided", 400, Instant.now());
    }


    public static CustomizedErrorDTO getDefaultResponse(String message) {
        return getDefaultResponse(message, 400, Instant.now());
    }

    public static CustomizedErrorDTO getDefaultResponse(String message, int code) {
        return getDefaultResponse(message, code, Instant.now());
    }
    
    public static CustomizedErrorDTO getDefaultResponse(String message, int code, Instant timestamp) {
        return CustomizedErrorDTO.builder()
                .message(message)
                .code(code)
                .timestamp(timestamp)
                .build();
    }
}
