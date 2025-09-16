package com.hedgerock.app_server.service;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ServiceConstants {
    public static final int TIME_TO_LIVE_IN_MINUTES = 10;
    public static final String RESTORE_PASSWORD_TOKEN_KEY = "restore";
    public static final String CONFIRM_EMAIL_TOKEN_KEY = "confirm";
}
