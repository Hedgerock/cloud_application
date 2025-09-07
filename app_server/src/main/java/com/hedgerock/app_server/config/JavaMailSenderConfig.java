package com.hedgerock.app_server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class JavaMailSenderConfig {

    @Value("${email.config.host}")
    private String MAIL_HOST;

    @Value("${email.config.port}")
    private int MAIL_PORT;

    @Value("${email.config.username}")
    private String MAIL_USERNAME;

    @Value("${email.config.password}")
    private String MAIL_PASSWORD;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

         mailSender.setHost(MAIL_HOST);
         mailSender.setPort(MAIL_PORT);
         mailSender.setUsername(MAIL_USERNAME);
         mailSender.setPassword(MAIL_PASSWORD);
         mailSender.setProtocol("smtp");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.starttls.required", true);
        props.put("mail.smtp.ssl.trust", MAIL_HOST);

         return mailSender;
    }

}
