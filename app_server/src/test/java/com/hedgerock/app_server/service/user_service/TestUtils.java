package com.hedgerock.app_server.service.user_service;

import com.hedgerock.app_server.config.constraints.types.TokenType;
import com.hedgerock.app_server.dto.Roles;
import com.hedgerock.app_server.dto.auth.RegisterDTO;
import com.hedgerock.app_server.dto.auth.validation.ValidationPasswordTokenDTO;
import com.hedgerock.app_server.dto.users.CurrentUserDTO;
import com.hedgerock.app_server.dto.users.UserDTO;
import com.hedgerock.app_server.entity.AuthoritiesEntity;
import com.hedgerock.app_server.entity.UserEntity;
import org.mockito.Mockito;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Collections;
import java.util.List;

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
