package com.hedgerock.app_server;

import com.hedgerock.app_server.config.constraints.types.TokenType;
import com.hedgerock.app_server.dto.Roles;
import com.hedgerock.app_server.dto.auth.RegisterDTO;
import com.hedgerock.app_server.dto.auth.validation.ValidationPasswordTokenDTO;
import com.hedgerock.app_server.dto.users.CurrentUserDTO;
import com.hedgerock.app_server.dto.users.UserDTO;
import com.hedgerock.app_server.entity.AuthoritiesEntity;
import com.hedgerock.app_server.entity.UserEntity;
import jakarta.mail.BodyPart;
import jakarta.mail.Header;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.mockito.Mockito;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class TestUtils {
    private TestUtils() {}

    public static String getKey(TokenType tokenType, String token) {
        return tokenType.getValue() + ":" + token;
    }

    public static String getValueAsString() {
        return """
                {"email": "%s", "password", "%s"}
                """.formatted(getEmail(), getEncodedPassword());
    }

    public static MimeMessage getMimeMessage() {
        return new MimeMessage(Session.getDefaultInstance(new Properties()));
    }

    public static void debugBodyPart(BodyPart bodyPart) throws Exception {
        System.out.println("=== BodyPart Debug ===");
        System.out.println("Content-Type: " + bodyPart.getContentType());
        System.out.println("Description: " + bodyPart.getDescription());
        System.out.println("Disposition: " + bodyPart.getDisposition());
        System.out.println("Size: " + bodyPart.getSize());
        System.out.println("Headers:");
        Enumeration<?> headers = bodyPart.getAllHeaders();
        while (headers.hasMoreElements()) {
            Header header = (Header) headers.nextElement();
            System.out.println("  " + header.getName() + ": " + header.getValue());
        }
        System.out.println("Content:");
        System.out.println(bodyPart.getContent());
        System.out.println("======================");
    }

    public static String getFromEmail(MimeMessage captured) throws Exception {
        return ((InternetAddress) captured.getFrom()[0]).getAddress();
    }

    public static String getToEmail(MimeMessage captured) throws Exception {
        return ((InternetAddress) captured.getRecipients(Message.RecipientType.TO)[0]).getAddress();
    }

    public static String getHtml(MimeMessage captured) throws Exception {
        final Object content = captured.getContent();
        return extractHtml(content);
    }

    public static String extractHtml(Object content) throws Exception {
        if (content instanceof String str) {
            return str;
        }

        if (content instanceof MimeMultipart mimeMultipart) {
            for (int i = 0; i < mimeMultipart.getCount(); i++) {
                BodyPart bodyPart = mimeMultipart.getBodyPart(i);
                Object partContent = bodyPart.getContent();

                if (bodyPart.getContentType().toLowerCase().contains("text/html")) {
                    return partContent instanceof String ? (String) partContent : null;
                }

                String nestedHtml = extractHtml(partContent);
                if (nestedHtml != null) {
                    return nestedHtml;
                }
            }
        }

        return null;
    }

    public static String getEncodedPassword() {
        return "$2a$10$5BfoT4H0PYztzw1gdBN0W.J.ZcAhr3pKezkRL8n/AXfByJFKQuKWW";
    }

    public static String getEmail() {
        return "hedgerock@gmail.com";
    }

    public static String getToken() {
        return "9374b2bf-bcea-49d0-af03-f360f7b84240";
    }

    @SuppressWarnings("unchecked")
    public static <K, V> ValueOperations<K, V> getOpsForValue() {
        return (ValueOperations<K, V>) Mockito.mock(ValueOperations.class);
    }

    public static AuthoritiesEntity getAuthoritiesEntity() {
        return new AuthoritiesEntity(
                1L,
                Roles.USER.name()
        );
    }

    public static RegisterDTO getRegisterDTO() {
        return new RegisterDTO(
                "hedgerock@gmail.com",
                getEncodedPassword()
        );
    }
    public static ValidationPasswordTokenDTO getValidationPasswordTokenDTO() {
        return new ValidationPasswordTokenDTO(
                "22c173d8-5853-4b14-98fa-14e821205a1b",
                "MyNewPassword"
        );
    }

    public static UserEntity getUserEntity() {
        return new UserEntity(
                1L,
                "hedgerock@gmail.com",
                "$2a$10$BX/p48JoXPNttp9rqlq4vOVnIFATL8.0CS5rT5cxzaH71SCjj/LK6",
                10737418240L,
                0L,
                Collections.emptyList(),
                List.of(
                        new AuthoritiesEntity(1L, "USER")
                )
        );
    }

    public static CurrentUserDTO getUserDTO() {
        return CurrentUserDTO.toDTO(getUserEntity());
    }
    public static List<UserEntity> getUserEntities() {
        return List.of(
                new UserEntity(
                        1L,
                        "hedgerock@gmail.com",
                        "$2a$10$BX/p48JoXPNttp9rqlq4vOVnIFATL8.0CS5rT5cxzaH71SCjj/LK6",
                        10737418240L,
                        0L,
                        Collections.emptyList(),
                        List.of(
                                new AuthoritiesEntity(1L, "USER")
                        )),
                new UserEntity(
                        2L,
                        "hedgerock@gmail.com",
                        "$2a$10$BX/p48JoXPNttp9rqlq4vOVnIFATL8.0CS5rT5cxzaH71SCjj/LK6",
                        10737418240L,
                        0L,
                        Collections.emptyList(),
                        List.of(
                                new AuthoritiesEntity(1L, "USER")
                        )),
                new UserEntity(
                        3L,
                        "hedgerock@gmail.com",
                        "$2a$10$BX/p48JoXPNttp9rqlq4vOVnIFATL8.0CS5rT5cxzaH71SCjj/LK6",
                        10737418240L,
                        0L,
                        Collections.emptyList(),
                        List.of(
                                new AuthoritiesEntity(1L, "USER")
                        ))

        );
    }

    public static List<UserDTO> getUserDTOs() {
        return getUserEntities().stream()
                .map(UserDTO::toDTO)
                .toList();
    }

}
