package com.hedgerock.app_server.config.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedgerock.app_server.config.utils.UtilityMethods;
import com.hedgerock.app_server.dto.errors.CustomizedErrorDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomerAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.getWriter().write(UtilityMethods.convertToJson(
                CustomizedErrorDTO.getDefaultResponse(), objectMapper
        ));
    }
}