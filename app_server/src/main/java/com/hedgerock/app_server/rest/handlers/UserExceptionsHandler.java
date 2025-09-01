package com.hedgerock.app_server.rest.handlers;

import com.hedgerock.app_server.dto.errors.CustomizedErrorDTO;
import com.hedgerock.app_server.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionsHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<CustomizedErrorDTO> handleUserNotFoundException(UserNotFoundException e) {

        final CustomizedErrorDTO errorDTO = CustomizedErrorDTO.getDefaultResponse(
                e.getMessage(), HttpStatus.NOT_FOUND.value()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(errorDTO);
    }

}
