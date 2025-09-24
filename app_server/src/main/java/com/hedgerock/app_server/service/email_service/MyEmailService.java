package com.hedgerock.app_server.service.email_service;

import com.hedgerock.app_server.dto.auth.RegisterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.hedgerock.app_server.service.ServiceUtils.loadHTML;
import static com.hedgerock.app_server.service.ServiceUtils.sendMimeMessage;

@Service
@RequiredArgsConstructor
public class MyEmailService implements EmailService {
    private static final String TEMPLATE_OF_CLIENT_PATH = "http://localhost:%s";

    private final JavaMailSender javaMailSender;

    @Value("${client.port.value}")
    private String CLIENT_PORT;

    @Value("${email.config.username}")
    private String FROM;

    @Override
    //TODO Remove RegisterDTO from method
    public void sendConfirmationEmail(String to, String token, RegisterDTO registerDTO) {
        final String CLIENT_PATH = String.format(TEMPLATE_OF_CLIENT_PATH, CLIENT_PORT);
        String html = loadHTML("email/confirmation");

        String subject = "Registration process";
        String confirmationUrl = String.format((CLIENT_PATH + "/auth/confirm?token=%s"), token);

        html = html.replace("{{confirmation_url}}", confirmationUrl);

        sendMimeMessage(FROM, to, subject, html, javaMailSender);
    }

    @Override
    public void sendRestorePasswordMessage(String to, String token) {
        final String CLIENT_PATH = String.format(TEMPLATE_OF_CLIENT_PATH, CLIENT_PORT);
        String html = loadHTML("email/restore_password");

        String subject = "Restore password process";
        String confirmationUrl = String.format((CLIENT_PATH + "/auth/restore_password?token=%s"), token);

        html = html.replace("{{restore_password_url}}", confirmationUrl);

        sendMimeMessage(FROM, to, subject, html, javaMailSender);
    }
}
