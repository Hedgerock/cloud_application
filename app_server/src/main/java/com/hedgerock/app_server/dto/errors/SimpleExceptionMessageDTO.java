package com.hedgerock.app_server.dto.errors;

import lombok.Builder;

@Builder
public record SimpleExceptionMessageDTO(String message) {

    public static<T extends RuntimeException> SimpleExceptionMessageDTO toDTO(T e) {
        return SimpleExceptionMessageDTO.builder()
                .message(e.getMessage())
                .build();
    }
}
