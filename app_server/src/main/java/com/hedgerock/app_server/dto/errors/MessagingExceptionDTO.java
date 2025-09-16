package com.hedgerock.app_server.dto.errors;

import com.hedgerock.app_server.exceptions.SendEmailException;
import lombok.Builder;

@Builder
public record MessagingExceptionDTO(String message, int code) {

    public static MessagingExceptionDTO toDTO(SendEmailException e) {
        return MessagingExceptionDTO.builder()
                .message(e.getMessage())
                .code(400)
                .build();
    }

}