package com.hedgerock.app_server.config.constraints.types;

import lombok.Getter;

@Getter
public enum TokenType {
    RESTORE_PASSWORD("restore"),
    CONFIRM_EMAIL("confirm");

    private final String value;
    TokenType(String value) {
        this.value = value;
    }
}
