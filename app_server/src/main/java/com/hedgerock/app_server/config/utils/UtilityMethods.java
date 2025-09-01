package com.hedgerock.app_server.config.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedgerock.app_server.dto.errors.CustomizedErrorDTO;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class UtilityMethods {

    public String convertToJson(CustomizedErrorDTO dto, ObjectMapper objectMapper) {

        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert DTO to Json {}", dto);
            return "";
        }
    }

}
