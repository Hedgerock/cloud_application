package com.hedgerock.app_server.rest.handlers;

import com.hedgerock.app_server.dto.errors.SimpleExceptionMessageDTO;
import com.hedgerock.app_server.exceptions.CacheValueException;
import com.hedgerock.app_server.exceptions.RoleNotFoundException;
import com.hedgerock.app_server.exceptions.UserConfirmException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UniqueExceptionHandler {

    @ExceptionHandler({ CacheValueException.class, RoleNotFoundException.class, UserConfirmException.class })
    public ResponseEntity<SimpleExceptionMessageDTO> handleCacheValueException(RuntimeException e) {
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(SimpleExceptionMessageDTO.toDTO(e));
    }
}
