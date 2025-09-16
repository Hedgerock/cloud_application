package com.hedgerock.app_server.exceptions;

import org.springframework.validation.BindingResult;

import java.util.List;

public class CurrentBindException extends RuntimeException {
    private final BindingResult bindingResult;

    public CurrentBindException(String message, BindingResult bindingResult) {
        super(message);
        this.bindingResult = bindingResult;
    }

    public List<String> getErrors() {

        return bindingResult.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
    }
}
