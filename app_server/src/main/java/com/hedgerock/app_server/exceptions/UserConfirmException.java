package com.hedgerock.app_server.exceptions;

public class UserConfirmException extends RuntimeException {
    public UserConfirmException(String message) {
        super(message);
    }
}
