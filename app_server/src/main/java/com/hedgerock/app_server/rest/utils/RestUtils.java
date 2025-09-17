package com.hedgerock.app_server.rest.utils;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class RestUtils {
    public static String getToken() {
        return UUID.randomUUID().toString();
    }
}
