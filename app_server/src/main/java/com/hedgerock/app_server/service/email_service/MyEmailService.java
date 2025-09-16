package com.hedgerock.app_server.service.email_service;

import com.hedgerock.app_server.dto.auth.RegisterDTO;
import com.hedgerock.app_server.dto.auth.RestorePasswordDTO;
import com.hedgerock.app_server.exceptions.SendEmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MyEmailService implements EmailService {
    private static final String TEMPLATE_OF_CLIENT_PATH = "http://localhost:%s";

    private final JavaMailSender javaMailSender;

    @Value("${client.port.value}")
    private String CLIENT_PORT;

    @Value("${email.config.username}")
    private String FROM;

    private String loadHTML(String filename) {
        try {
            ClassPathResource classPathResource = new ClassPathResource("templates/" + filename + ".html");

            try(InputStream inputStream = classPathResource.getInputStream()) {
                return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }

        } catch (IOException e) {
            throw new NoSuchElementException("Html with name " + filename + " not found" );
        }
    }

    private void sendMimeMessage(String to, String subject, String html) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            helper.setFrom(FROM);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new SendEmailException("Can't send email to: " + to);
        }
    }

    @Override
    public void sendConfirmationEmail(String to, String token, RegisterDTO registerDTO) {
        final String CLIENT_PATH = String.format(TEMPLATE_OF_CLIENT_PATH, CLIENT_PORT);
        String html = loadHTML("email/confirmation");

        String subject = "Registration process";
        String confirmationUrl = String.format((CLIENT_PATH + "/auth/confirm?token=%s"), token);

        html = html.replace("{{confirmation_url}}", confirmationUrl);

        sendMimeMessage(to, subject, html);
    }

    @Override
    public void sendRestorePasswordMessage(String to, String token) {
        final String CLIENT_PATH = String.format(TEMPLATE_OF_CLIENT_PATH, CLIENT_PORT);
        String html = loadHTML("email/restore_password");

        String subject = "Restore password process";
        String confirmationUrl = String.format((CLIENT_PATH + "/auth/restore_password?token=%s"), token);

        html = html.replace("{{restore_password_url}}", confirmationUrl);

        sendMimeMessage(to, subject, html);
    }
}
