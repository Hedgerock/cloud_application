package com.hedgerock.app_server.service.user_service;

import com.hedgerock.app_server.dto.auth.RegisterDTO;
import com.hedgerock.app_server.dto.users.CurrentUserDTO;
import com.hedgerock.app_server.dto.users.UserDTO;

import java.util.List;

public interface UserService {
    CurrentUserDTO getById(Long id);
    List<UserDTO> getAll();

    void cachePendingUser(String token, RegisterDTO registerDTO);
    void confirmUser(String token);
}
