package com.hedgerock.app_server.service.email_service;

import com.hedgerock.app_server.dto.auth.RegisterDTO;
import com.hedgerock.app_server.service.user_service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MyEmailService implements EmailService {

    private final JavaMailSender javaMailSender;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${client.port.value}")
    private String CLIENT_PORT;

    @Value("${email.config.username}")
    private String FROM;

    private String loadConfirmationHtml(String filename) {
        try {
            ClassPathResource classPathResource = new ClassPathResource("templates/email/" + filename + ".html");

            try(InputStream inputStream = classPathResource.getInputStream()) {
                return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }

        } catch (IOException e) {
            throw new NoSuchElementException("Html with name " + filename + " not found" );
        }
    }

    @Override
    public void sendConfirmationEmail(String to, String token, RegisterDTO registerDTO) {
        final String CLIENT_PATH = "http://localhost:" + CLIENT_PORT;
        String html = loadConfirmationHtml("confirmation");

        String subject = "Registration process";
        String confirmationUrl = String.format((CLIENT_PATH + "/confirm?token=%s"), token);

        html = html.replace("{{confirmation_url}}", confirmationUrl);

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            helper.setFrom(FROM);

            javaMailSender.send(mimeMessage);
            userService.cachePendingUser(token, registerDTO.getEncryptedDTO(passwordEncoder));
        } catch (MessagingException e) {
            throw new IllegalStateException("Can't send email to: " + to);
        }
    }
}
