package com.hedgerock.app_server.service.email_service;

import com.hedgerock.app_server.TestUtils;
import com.hedgerock.app_server.dto.auth.RegisterDTO;
import com.hedgerock.app_server.exceptions.SendEmailException;
import com.hedgerock.app_server.service.ServiceUtils;
import jakarta.mail.internet.MimeMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.NoSuchElementException;

import static com.hedgerock.app_server.TestUtils.*;
import static com.hedgerock.app_server.service.ServiceUtils.loadHTML;
import static com.hedgerock.app_server.service.ServiceUtils.sendMimeMessage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MyEmailServiceTests {
    @Mock
    private JavaMailSender javaMailSender;

    @Captor
    private ArgumentCaptor<MimeMessage> mimeMessageArgumentCaptor;

    @InjectMocks
    private MyEmailService myEmailService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(myEmailService, "CLIENT_PORT", "8080");
        ReflectionTestUtils.setField(myEmailService, "FROM", "noreply@gmail.com");
    }

    @Test
    @DisplayName("Send restore password email test case fail to send email")
    void givenToAndToken_whenSendRestorePasswordMessage_thenThrowsSendEmailException() {
        try(MockedStatic<ServiceUtils> mockedStatic = Mockito.mockStatic(ServiceUtils.class)) {
            //given
            final String email = getEmail();
            final String html = "<html>{{restore_password_url}}</html>";
            final String token = getToken();

            mockedStatic.when(() -> loadHTML("email/restore_password"))
                    .thenReturn(html);

            mockedStatic.when(() ->
                            sendMimeMessage(
                                    anyString(),
                                    anyString(),
                                    anyString(),
                                    anyString(),
                                    any(JavaMailSender.class)
                            )
                    )
                    .thenThrow(new SendEmailException("Can't send email to: " + email));

            final MyEmailService spyEmailService = Mockito.spy(myEmailService);

            //when then
            Assertions.assertThatThrownBy(() ->
                            spyEmailService.sendRestorePasswordMessage(email, token)
                    )
                    .isInstanceOf(SendEmailException.class)
                    .hasMessageContaining("Can't send email to: " + email);
        }
    }

    @Test
    @DisplayName("Send restore password email test case html not found")
    void givenToAndToken_whenSendRestorePasswordMessage_thenThrowsNoSuchElementException() {
        try(MockedStatic<ServiceUtils> mockedStatic = Mockito.mockStatic(ServiceUtils.class)) {
            //given
            final String email = getEmail();
            final String token = getToken();
            final String invalidPath = "email/restore_password";

            mockedStatic.when(() -> loadHTML(invalidPath))
                    .thenThrow(new NoSuchElementException("Html with name " + invalidPath + " not found"));

            final MyEmailService spyEmailService = Mockito.spy(myEmailService);

            //when then
            Assertions.assertThatThrownBy(() ->
                            spyEmailService.sendRestorePasswordMessage(email, token)
                    )
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessageContaining("Html with name " + invalidPath + " not found");

        }
    }

    @Test
    @DisplayName("Send restore password email test")
    void givenToAndToken_whenSendRestorePasswordMessage_thenSuccessfullySentEmail() throws Exception {
        //given
        final String email = getEmail();
        final String token = getToken();
        final MimeMessage mimeMessage = TestUtils.getMimeMessage();
        final String html =
                String.format("http://localhost:8080/auth/restore_password?token=%s", token);

        BDDMockito.given(javaMailSender.createMimeMessage())
                .willReturn(mimeMessage);

        BDDMockito.doNothing().when(javaMailSender)
                .send(any(MimeMessage.class));
        //when
        myEmailService.sendRestorePasswordMessage(email, token);
        //then
        verify(javaMailSender).send(mimeMessageArgumentCaptor.capture());

        final MimeMessage captured = mimeMessageArgumentCaptor.getValue();
        final String FROM_EMAIL = TestUtils.getFromEmail(captured);
        final String TO_EMAIL = TestUtils.getToEmail(captured);
        final String content = TestUtils.getHtml(captured);
        final String subject = captured.getSubject();

        assertThat("Restore password process").isEqualTo(subject);
        assertThat("noreply@gmail.com").isEqualTo(FROM_EMAIL);
        assertThat("hedgerock@gmail.com").isEqualTo(TO_EMAIL);

        assertNotNull(content);
        assertTrue(content.contains(html));
    }

    @Test
    @DisplayName("Send registration confirmation email test case fail to send email")
    void givenToTokenAndRegisterDTO_whenSendConfirmationEmail_thenThrowsSendEmailException() {
        try(MockedStatic<ServiceUtils> mockedStatic = Mockito.mockStatic(ServiceUtils.class)) {
            //given
            final RegisterDTO registerDTO = TestUtils.getRegisterDTO();
            final String html = "<html>{{confirmation_url}}</html>";
            final String token = getToken();

            mockedStatic.when(() -> loadHTML("email/confirmation"))
                    .thenReturn(html);

            mockedStatic.when(() ->
                            sendMimeMessage(
                                    anyString(),
                                    anyString(),
                                    anyString(),
                                    anyString(),
                                    any(JavaMailSender.class)
                            )
                    )
                    .thenThrow(new SendEmailException("Can't send email to: " + registerDTO.email()));

            final MyEmailService spyEmailService = Mockito.spy(myEmailService);

            //when then
            Assertions.assertThatThrownBy(() ->
                            spyEmailService.sendConfirmationEmail(registerDTO.email(), token, registerDTO)
                    )
                    .isInstanceOf(SendEmailException.class)
                    .hasMessageContaining("Can't send email to: " + registerDTO.email());
        }
    }

    @Test
    @DisplayName("Send registration confirmation email test case html not found")
    void givenToTokenAndRegisterDTO_whenSendConfirmationEmail_thenThrowsNoSuchElementException() {
        try(MockedStatic<ServiceUtils> mockedStatic = Mockito.mockStatic(ServiceUtils.class)) {
            //given
            final String email = getEmail();
            final String token = getToken();
            final RegisterDTO registerDTO = getRegisterDTO();
            final String invalidPath = "email/confirmation";

            mockedStatic.when(() -> loadHTML(invalidPath))
                    .thenThrow(new NoSuchElementException("Html with name " + invalidPath + " not found"));

            final MyEmailService spyEmailService = Mockito.spy(myEmailService);

            //when then
            Assertions.assertThatThrownBy(() ->
                            spyEmailService.sendConfirmationEmail(email, token, registerDTO)
                    )
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessageContaining("Html with name " + invalidPath + " not found");

            Mockito.verify(javaMailSender, never())
                    .send(any(MimeMessage.class));
        }
    }

    @Test
    @DisplayName("Send registration confirmation email test")
    void givenToTokenAndRegisterDTO_whenSendConfirmationEmail_thenSuccessfullySentEmail() throws Exception {
        //given
        final RegisterDTO registerDTO = TestUtils.getRegisterDTO();
        final String token = getToken();
        final MimeMessage mimeMessage = TestUtils.getMimeMessage();
        final String html =
                String.format("http://localhost:8080/auth/confirm?token=%s", token);

        BDDMockito.given(javaMailSender.createMimeMessage())
                        .willReturn(mimeMessage);

        BDDMockito.doNothing().when(javaMailSender)
                .send(any(MimeMessage.class));
        //when
        myEmailService.sendConfirmationEmail(registerDTO.email(), token, registerDTO);
        //then
        verify(javaMailSender).send(mimeMessageArgumentCaptor.capture());

        final MimeMessage captured = mimeMessageArgumentCaptor.getValue();
        final String FROM_EMAIL = TestUtils.getFromEmail(captured);
        final String TO_EMAIL = TestUtils.getToEmail(captured);
        final String content = TestUtils.getHtml(captured);
        final String subject = captured.getSubject();

        assertThat("Registration process").isEqualTo(subject);
        assertThat("noreply@gmail.com").isEqualTo(FROM_EMAIL);
        assertThat("hedgerock@gmail.com").isEqualTo(TO_EMAIL);

        assertNotNull(content);
        assertTrue(content.contains(html));
    }
}
