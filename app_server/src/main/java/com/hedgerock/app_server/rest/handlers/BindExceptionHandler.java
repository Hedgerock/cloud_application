package com.hedgerock.app_server.rest.handlers;

import com.hedgerock.app_server.dto.errors.BindExceptionDTO;
import com.hedgerock.app_server.exceptions.CurrentBindException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BindExceptionHandler {

    @ExceptionHandler(CurrentBindException.class)
    public ResponseEntity<BindExceptionDTO> handleCurrentBindException(CurrentBindException e) {
        final BindExceptionDTO exceptionDTO = BindExceptionDTO.toDTO(e);

        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(exceptionDTO);
    }

}
