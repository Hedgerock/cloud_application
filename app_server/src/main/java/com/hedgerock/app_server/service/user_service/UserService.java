package com.hedgerock.app_server.service.user_service;

import com.hedgerock.app_server.config.constraints.types.TokenType;
import com.hedgerock.app_server.dto.auth.RegisterDTO;
import com.hedgerock.app_server.dto.auth.validation.ValidationPasswordTokenDTO;
import com.hedgerock.app_server.dto.users.CurrentUserDTO;
import com.hedgerock.app_server.dto.users.UserDTO;
import com.hedgerock.app_server.entity.UserEntity;
import org.apache.el.parser.Token;

import java.util.List;

public interface UserService {
    CurrentUserDTO getById(Long id);
    List<UserDTO> getAll();

    void cachePendingUser(String token, RegisterDTO encryptedDTO);
    void cachePendingEmailForRestore(TokenType tokenType, String token, String email);
    void confirmUser(String token);
    void confirmPassword(ValidationPasswordTokenDTO validationPasswordTokenDTO);
}
