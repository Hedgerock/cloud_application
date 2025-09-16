package com.hedgerock.app_server.rest.handlers;

import com.hedgerock.app_server.dto.errors.InvalidTokenExceptionDTO;
import com.hedgerock.app_server.exceptions.InvalidTokenException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InvalidTokenExceptionHandler {

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<InvalidTokenExceptionDTO> handle(InvalidTokenException e, HttpServletResponse response) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(InvalidTokenExceptionDTO.toDTO(e, response.getStatus()));
    }
}
