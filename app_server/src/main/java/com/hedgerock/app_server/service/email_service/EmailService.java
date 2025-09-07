package com.hedgerock.app_server.service.email_service;

import com.hedgerock.app_server.dto.auth.RegisterDTO;

public interface EmailService {

    void sendConfirmationEmail(String to, String token, RegisterDTO registerDTO);

}
