package com.hedgerock.app_server.rest.handlers;

import com.hedgerock.app_server.dto.errors.MessagingExceptionDTO;
import com.hedgerock.app_server.exceptions.SendEmailException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MessagingExceptionHandler {

    @ExceptionHandler(SendEmailException.class)
    public ResponseEntity<MessagingExceptionDTO> handleMessagingException(SendEmailException e) {

        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(MessagingExceptionDTO.toDTO(e));
    }
}
