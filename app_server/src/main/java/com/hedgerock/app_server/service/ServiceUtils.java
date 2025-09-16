package com.hedgerock.app_server.service;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ServiceUtils {
    public static String getKeyToken(String key, String token) {
        return key + ":" + token;
    }
}
