package com.hedgerock.app_server.service.user_service;

import com.hedgerock.app_server.config.constraints.types.TokenType;
import com.hedgerock.app_server.dto.auth.RegisterDTO;
import com.hedgerock.app_server.dto.auth.validation.ValidationPasswordTokenDTO;
import com.hedgerock.app_server.dto.users.CurrentUserDTO;
import com.hedgerock.app_server.dto.users.UserDTO;

import java.util.List;

public interface UserService {
    CurrentUserDTO getById(Long id);
    List<UserDTO> getAll();

    void cachePendingUser(TokenType tokenType, String token, RegisterDTO encryptedDTO);
    void cachePendingEmailForRestore(TokenType tokenType, String token, String email);
    void confirmUser(TokenType tokenType, String token);
    void confirmPassword(TokenType tokenType, ValidationPasswordTokenDTO validationPasswordTokenDTO);
}
