package com.hedgerock.app_server.service;

import com.hedgerock.app_server.exceptions.SendEmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;

@UtilityClass
public class ServiceUtils {
    public static String getKeyToken(String key, String token) {
        return key + ":" + token;
    }

    @SafeVarargs
    public static<K, V> void deleteRedisKeys(RedisTemplate<K, V> redisTemplate, K... keys) {
        redisTemplate.delete(List.of(keys));
    }

    public static String loadHTML(String filename) {
        try {
            ClassPathResource classPathResource = new ClassPathResource("templates/" + filename + ".html");

            try(InputStream inputStream = classPathResource.getInputStream()) {
                return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }

        } catch (IOException e) {
            throw new NoSuchElementException("Html with name " + filename + " not found" );
        }
    }

    public static void sendMimeMessage(
            String from, String to, String subject, String html, JavaMailSender javaMailSender) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            helper.setFrom(from);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new SendEmailException("Can't send email to: " + to);
        }
    }
}
