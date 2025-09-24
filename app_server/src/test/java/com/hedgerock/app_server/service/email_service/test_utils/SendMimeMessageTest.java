package com.hedgerock.app_server.service.email_service.test_utils;

import com.hedgerock.app_server.TestUtils;
import com.hedgerock.app_server.exceptions.SendEmailException;
import com.hedgerock.app_server.service.ServiceUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
public class SendMimeMessageTest {

    @Mock
    JavaMailSender javaMailSender;

    @Test
    @DisplayName("SendMimeMessage test case when message by reason wasn't sent")
    void givenCredentialsToSend_whenSendMimeMessage_thenThrowsSendEmailException() {
        //given
        String email = TestUtils.getEmail();
        MimeMessage mimeMessage = TestUtils.getMimeMessage();
        String fromEmail = "noreply@gmail.com";
        String subject = "Current subject";
        String html = "<html>Hello world</html>";

        BDDMockito.given(javaMailSender.createMimeMessage())
                        .willReturn(mimeMessage);

        doAnswer(_ -> {
            throw new MessagingException();
        }).when(javaMailSender).send(any(MimeMessage.class));

        //when then
        Assertions.assertThatThrownBy(() ->
                        ServiceUtils.sendMimeMessage(
                                fromEmail,
                                email,
                                subject,
                                html,
                                javaMailSender
                        )
        )
        .isInstanceOf(SendEmailException.class)
        .hasMessageContaining("Can't send email to: " + email);
    }
}
